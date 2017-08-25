package answer.king.helpers;

import com.google.common.collect.Lists;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import answer.king.model.Item;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;

public class TestHelpers {

  public static final Logger LOG = LoggerFactory.getLogger(TestHelpers.class);

  public static final String UNIT_TEST_ITEM_NAME = "Angus Burger";
  public static final BigDecimal UNIT_TEST_PRICE = new BigDecimal("3.99"); //using string BigDecimal in test as to not worry about precision
  public static final Long UNIT_TEST_ID = 1l;
  public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
  public static final double UNIT_TEST_PRICE_COMPARE = 3.99;
  public static final int UNIT_TEST_ID_COMPARE = 1;

  public static String createJsonFromObject(Object obj) {
    ObjectMapper mapper = new ObjectMapper();
    String returnString = "";
    try {
      returnString = mapper.writeValueAsString(obj);
    } catch (JsonProcessingException exception) {
      LOG.error("Error converting to JSON: ", exception);
    }
    return returnString;
  }

  public static Item createItem() {
    Item item = new Item();
    item.setId(UNIT_TEST_ID);
    item.setPrice(UNIT_TEST_PRICE);
    item.setName(UNIT_TEST_ITEM_NAME);
    return item;
  }

  public static LineItem createLineItem() {
    LineItem lineItem = new LineItem();
    lineItem.setQuantity(1);
    lineItem.setId(UNIT_TEST_ID);
    lineItem.setItem(createItem());
    lineItem.setPrice(lineItem.getItem().getPrice());
    return lineItem;
  }

  public static Order createOrder() {
    LineItem item = createLineItem();
    LineItem secondItem = createLineItem();
    secondItem.setId(2L);
    secondItem.getItem().setId(2L);
    secondItem.getItem().setName("fries");
    secondItem.getItem().setPrice(new BigDecimal("2.99"));
    Order order = new Order();
    order.setId(UNIT_TEST_ID);
    order.setLineItems(Lists.newArrayList(item, secondItem));
    return order;
  }

  public static Receipt createReceipt() {
    Receipt receipt = new Receipt();
    receipt.setOrder(createOrder());
    receipt.setPayment(new BigDecimal("8.00"));
    return receipt;
  }


}