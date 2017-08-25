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
import answer.king.repo.ItemRepository;
import answer.king.repo.LineItemRepository;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext
public class LineItemRepositoryTest {

  @Autowired
  LineItemRepository lineItemRepository;

  @Autowired
  ItemRepository itemRepository;

  @After
  public void tearDown() {
    deleteData();
  }

  @Test
  public void insertAndUpdateLineItem() {
    Item item = TestHelpers.createItem();
    item = itemRepository.save(item);
    LineItem lineItem = TestHelpers.createLineItem();
    lineItem.setItem(item);
    lineItem = lineItemRepository.save(lineItem);
    assertEquals("LineItem returned from database generates new id", Long.valueOf("1"), lineItem.getId());
    lineItem.setPrice(new BigDecimal("0.05"));
    lineItem = lineItemRepository.save(lineItem);
    assertEquals("Update of item has new price", new BigDecimal("0.05"), lineItem.getPrice());
  }

  private void deleteData() {
    lineItemRepository.deleteAll();
    itemRepository.deleteAll();
  }

}
