package answer.king.controller;


import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import answer.king.exception.BusinessRulesException;
import answer.king.exception.handler.GlobalExceptionHandler;
import answer.king.helpers.TestHelpers;
import answer.king.model.Item;
import answer.king.service.ItemService;
import answer.king.service.ItemServiceImpl;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({ItemController.class, ItemServiceImpl.class})
public class ItemControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private ItemService itemService;

  @Autowired
  ItemController itemController;

  @Before
  public void setUpMockMvc() {
    mockMvc = MockMvcBuilders.standaloneSetup(itemController).setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  public void testsSimpleCreateWithValidData() throws Exception, BusinessRulesException {
    Item savedItem = TestHelpers.createItem();
    Item returnedItem = TestHelpers.createItem();
    Mockito.when(itemService.save(savedItem)).thenReturn(returnedItem);
    mockMvc.perform(post("/item").contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelpers.createJsonFromObject(savedItem)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(TestHelpers.UNIT_TEST_ID_COMPARE)))
        .andExpect(jsonPath("name", is(TestHelpers.UNIT_TEST_ITEM_NAME)))
        .andExpect(jsonPath("price", is(TestHelpers.UNIT_TEST_PRICE_COMPARE)));
  }


  @Test
  public void testSimpleGetAll() throws Exception {
    Item returnedItem = TestHelpers.createItem();
    Mockito.when(itemService.getAll()).thenReturn(Lists.newArrayList(returnedItem));
    mockMvc.perform(get("/item")).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(TestHelpers.UNIT_TEST_ID_COMPARE)))
        .andExpect(jsonPath("$[0].name", is(TestHelpers.UNIT_TEST_ITEM_NAME)))
        .andExpect(jsonPath("$[0].price", is(TestHelpers.UNIT_TEST_PRICE_COMPARE)));
  }

  @Test
  public void testPriceUpdate() throws Exception, BusinessRulesException {
    Item savedItem = TestHelpers.createItem();
    Mockito.when(itemService.save(savedItem)).thenReturn(savedItem);
    mockMvc.perform(put("/item/1").contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelpers.createJsonFromObject(savedItem))).andExpect(status().isOk())
        .andExpect(jsonPath("id", is(TestHelpers.UNIT_TEST_ID_COMPARE)))
        .andExpect(jsonPath("name", is(TestHelpers.UNIT_TEST_ITEM_NAME)))
        .andExpect(jsonPath("price", is(TestHelpers.UNIT_TEST_PRICE_COMPARE)));
  }

  @Test
  public void testPWhenErrorThrowErrorMessageInBodyISReturnedCorrectly() throws Exception, BusinessRulesException {
    Item savedItem = TestHelpers.createItem();
    savedItem.setName("Burger 1");
    Mockito.when(itemService.save(savedItem)).thenThrow(new BusinessRulesException("Name can only contain letters A-Z;"));
    mockMvc.perform(put("/item/1").contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelpers.createJsonFromObject(savedItem))).andExpect(status().isBadRequest())
        .andExpect(jsonPath("code", is(TestHelpers.VALIDATION_ERROR)))
        .andExpect(jsonPath("message", is("Name can only contain letters A-Z;")));
  }
}