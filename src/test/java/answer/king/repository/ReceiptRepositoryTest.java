package answer.king.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import answer.king.helpers.TestHelpers;
import answer.king.model.Item;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.repo.LineItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.ReceiptRepository;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext
public class ReceiptRepositoryTest {

  @Autowired
  ReceiptRepository receiptRepository;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  ItemRepository itemRepository;

  @Autowired
  LineItemRepository lineItemRepository;

  @After
  public void tearDown() {
    deleteData();
  }

  //not strictly a unit test and more of an integration (with out having external dependencies) but valuable
  @Test
  public void saveReceiptAndGetReceiptBackFromH2() {
    Item item = TestHelpers.createItem();
    item = itemRepository.save(item);
    Item secondItem = TestHelpers.createItem();
    secondItem.setId(2L);
    secondItem.setName("fries");
    secondItem.setPrice(new BigDecimal("2.99"));
    secondItem = itemRepository.save(secondItem);
    LineItem lineItem = TestHelpers.createLineItem();
    lineItem.setItem(item);
    lineItemRepository.save(lineItem);
    LineItem secondLineItem = TestHelpers.createLineItem();
    secondLineItem.setId(2L);
    secondLineItem.setItem(secondItem);
    lineItemRepository.save(secondLineItem);
    Order order = TestHelpers.createOrder();
    orderRepository.save(order);
    Receipt receipt = TestHelpers.createReceipt();
    Receipt returnedReceipt = receiptRepository.save(receipt);
    assertEquals("Receipt returned from database generates new id", Long.valueOf("1"), returnedReceipt.getId());
  }

  private void deleteData() {
    lineItemRepository.deleteAll();
    itemRepository.deleteAll();
    orderRepository.deleteAll();
    receiptRepository.deleteAll();
  }

}
