package answer.king.repository;

import org.h2.server.web.WebServlet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import answer.king.helpers.TestHelpers;
import answer.king.model.Item;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.ReceiptRepository;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@DirtiesContext
public class ItemRepositoryTest {

  @Autowired
  ItemRepository itemRepository;

  @After
  public void tearDown() {
    deleteData();
  }

  @Test
  public void insertAndUpdateItem() {
    Item item = TestHelpers.createItem();
    item = itemRepository.save(item);
    assertEquals("Item returned from database generates new id", Long.valueOf("1"), item.getId());
    item.setPrice(new BigDecimal("0.05"));
    item = itemRepository.save(item);
    assertEquals("Update of item has new price",new BigDecimal("0.05"), item.getPrice());
  }

  private void deleteData() {
    itemRepository.deleteAll();
  }

}
