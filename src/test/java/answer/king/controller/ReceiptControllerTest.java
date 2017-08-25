package answer.king.controller;


import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import answer.king.exception.BusinessRulesException;
import answer.king.exception.handler.GlobalExceptionHandler;
import answer.king.helpers.TestHelpers;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.service.ReceiptService;
import answer.king.service.ReceiptServiceImpl;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({ReceiptController.class, ReceiptServiceImpl.class})
public class ReceiptControllerTest {

  private MockMvc mockMvc;

  @MockBean
  private ReceiptService receiptService;

  @Autowired
  ReceiptController receiptController;

  @Before
  public void setUpMockMvc() {
    mockMvc = MockMvcBuilders.standaloneSetup(receiptController).setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  public void testsGetReceiptById() throws Exception, BusinessRulesException {
    Receipt receipt = new Receipt();
    Order order = new Order();
    order.setId(TestHelpers.UNIT_TEST_ID);
    LineItem firstItem = TestHelpers.createLineItem();
    LineItem secondItem = TestHelpers.createLineItem();
    order.setLineItems(Lists.newArrayList(firstItem, secondItem));
    receipt.setOrder(order);
    receipt.setId(TestHelpers.UNIT_TEST_ID);
    receipt.setPayment(new BigDecimal("8.00"));
    Mockito.when(receiptController.getById(any(Long.class))).thenReturn(receipt);
    mockMvc.perform(get("/receipt/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("id", is(1)));
  }
}