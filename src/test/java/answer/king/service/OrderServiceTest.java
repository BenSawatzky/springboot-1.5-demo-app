package answer.king.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import answer.king.exception.BusinessRulesException;
import answer.king.helpers.TestHelpers;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.repo.LineItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.ReceiptRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
public class OrderServiceTest {

  @TestConfiguration
  static class OrderServiceImplTestContextConfiguration {

    @Bean
    public Validator validator() {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      return factory.getValidator();
    }

    @Bean
    public OrderService orderService() {
      return new OrderServiceImpl();
    }
  }

  @Autowired
  OrderService orderService;

  @MockBean
  private OrderRepository orderRepository;

  @MockBean
  private ItemRepository itemRepository;

  @MockBean
  private ReceiptRepository receiptRepository;

  @MockBean
  private LineItemRepository lineItemRepository;

  @Test
  public void testServicePay() throws Exception, BusinessRulesException {
    Order order = TestHelpers.createOrder();
    order.getLineItems().get(0).setQuantity(2);
    Mockito.when(orderRepository.findOne(any(Long.class))).thenReturn(order);
    Receipt returnedReceipt = orderService.pay(TestHelpers.UNIT_TEST_ID, new BigDecimal("20.00"));
    assertEquals("When paying order the correct change is returned", new BigDecimal("8.03"), returnedReceipt.getChange());
  }

  @Test
  public void testServicePaidIsSetOnValidPaymentData() throws Exception, BusinessRulesException {
    Order order = TestHelpers.createOrder();
    Mockito.when(orderRepository.findOne(any(Long.class))).thenReturn(order);
    Receipt returnedReceipt = orderService.pay(TestHelpers.UNIT_TEST_ID, new BigDecimal("8.00"));
    assertTrue("Order is paid upon happy path", returnedReceipt.getOrder().getPaid());
  }

  @Test(expected = BusinessRulesException.class)
  public void testServicePaidIsSetOnPaymentLessThanWhatIsOwed() throws Exception, BusinessRulesException {
    Order order = TestHelpers.createOrder();
    Mockito.when(orderRepository.findOne(any(Long.class))).thenReturn(order);
    orderService.pay(TestHelpers.UNIT_TEST_ID, new BigDecimal("4.00"));
  }

  @Test(expected = BusinessRulesException.class)
  public void testServicePaidIsSetOnPaymentEqualsWhatIsOwed() throws Exception, BusinessRulesException {
    Order order = TestHelpers.createOrder();
    Mockito.when(orderRepository.findOne(any(Long.class))).thenReturn(order);
    orderService.pay(TestHelpers.UNIT_TEST_ID, new BigDecimal("3.99"));
  }

  @Test
  public void testAddingNewLineItemAlreadyOnOrderAddToQuantity() throws Exception, BusinessRulesException {
    LineItem lineItem = TestHelpers.createLineItem();//creates quantity 1 and id 1
    Order order = TestHelpers.createOrder(); // create orders with two line item one being quantity 1 and id 1
    Mockito.when(orderRepository.findOne(any(Long.class))).thenReturn(order);
    Mockito.when(lineItemRepository.save(lineItem)).thenReturn(lineItem);
    lineItem = orderService.addItem(TestHelpers.UNIT_TEST_ID, lineItem); // already exists in TestHelpers.createOrder()
    assertEquals("LineItem 1 of TestHelpers.createOrder() should have double the quantity", Integer.valueOf("2"), lineItem.getQuantity());
  }

  @Test
  public void testAddingNewLineItemNotAlreadyOnOrder() throws Exception, BusinessRulesException {
    LineItem lineItem = TestHelpers.createLineItem();//creates quantity 1 and id 1
    Order order = TestHelpers.createOrder(); // create orders with two line item one being quantity 1 and id 1
    order.setLineItems(null); //clear so that new Line Item is only one
    Mockito.when(orderRepository.findOne(any(Long.class))).thenReturn(order);
    Mockito.when(lineItemRepository.save(lineItem)).thenReturn(lineItem);
    lineItem = orderService.addItem(TestHelpers.UNIT_TEST_ID, lineItem); // already exists in TestHelpers.createOrder()
    assertEquals("LineItem should have quantity of LineItem passed in", Integer.valueOf("1"), lineItem.getQuantity());
  }

  @Test
  public void testUnlikelyScenarioOfDatabaseReturningDuplicateLineItems() throws Exception, BusinessRulesException {
    LineItem lineItem = TestHelpers.createLineItem();//creates quantity 1 and id 1
    Order order = TestHelpers.createOrder(); // create orders with two line item one being quantity 1 and id 1
    order.addLineItem(lineItem); //add and lineItem1 again
    Mockito.when(orderRepository.findOne(any(Long.class))).thenReturn(order);
    Mockito.when(lineItemRepository.save(lineItem)).thenReturn(lineItem);
    lineItem = orderService.addItem(TestHelpers.UNIT_TEST_ID, lineItem); // already exists in TestHelpers.createOrder()
    assertEquals("LineItem 1 of TestHelpers.createOrder() should have triple the quantity", Integer.valueOf("3"), lineItem.getQuantity());
  }
}