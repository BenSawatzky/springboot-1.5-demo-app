package answer.king.controller;


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

import java.math.BigDecimal;

import answer.king.exception.BusinessRulesException;
import answer.king.exception.handler.GlobalExceptionHandler;
import answer.king.helpers.TestHelpers;
import answer.king.model.LineItem;
import answer.king.model.Receipt;
import answer.king.service.OrderService;
import answer.king.service.OrderServiceImpl;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({OrderController.class,OrderServiceImpl.class})
public class OrderControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private OrderService orderService;

  @Autowired
  OrderController orderController;

  @Before
  public void setUpMockMvc() {
    mockMvc = MockMvcBuilders.standaloneSetup(orderController).setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  public void testsSimplePaymentValidData() throws Exception, BusinessRulesException {
    Receipt receipt = TestHelpers.createReceipt();
    Mockito.when(orderService.pay(any(Long.class), any(BigDecimal.class))).thenReturn(receipt);
    mockMvc.perform(put("/order/1/pay").contentType(MediaType.APPLICATION_JSON)
                        .content("8.00"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("change", is(0.02)));
  }

  @Test
  public void testLineItemAddReturnLineItem() throws Exception, BusinessRulesException {
    Receipt receipt = TestHelpers.createReceipt();
    LineItem lineItem = TestHelpers.createLineItem();
    Mockito.when(orderService.addItem(TestHelpers.UNIT_TEST_ID , lineItem)).thenReturn(lineItem);
    mockMvc.perform(post("/order/1/lineitem").contentType(MediaType.APPLICATION_JSON)
                        .content(TestHelpers.createJsonFromObject(lineItem)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(1)))
        .andExpect(jsonPath("quantity", is(1)));
  }
}