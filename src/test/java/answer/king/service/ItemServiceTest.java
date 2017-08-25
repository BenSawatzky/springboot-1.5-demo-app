package answer.king.service;

import com.google.common.collect.Lists;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import answer.king.exception.BusinessRulesException;
import answer.king.helpers.TestHelpers;
import answer.king.model.Item;
import answer.king.repo.ItemRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class ItemServiceTest {


  @TestConfiguration
  static class ItemServiceImplTestContextConfiguration {

    @Bean
    public ItemService itemService() {
      return new ItemServiceImpl();
    }

    @Bean
    public Validator validator() {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      return factory.getValidator();
    }
  }

  @Autowired
  ItemService itemService;

  @MockBean
  private ItemRepository itemRepository;

  @Test
  public void testServiceSaveItem() throws Exception, BusinessRulesException {
    Item savedItem = TestHelpers.createItem();
    Item mockItem = TestHelpers.createItem();
    Mockito.when(itemRepository.save(savedItem)).thenReturn(mockItem);
    Item returnedItem = itemService.save(savedItem);
    assertEquals("Item returned from repository is equal to the Mock", mockItem, returnedItem);
  }

  @Test
  public void testServiceGetAllItems() throws Exception {
    Item mockItem = TestHelpers.createItem();
    Mockito.when(itemRepository.findAll()).thenReturn(Lists.newArrayList(mockItem));
    List<Item> returnedItems = itemService.getAll();
    assertEquals("Item returned from repository is equal to the Mock", mockItem.getId(), returnedItems.get(0).getId());
  }

  @Test
  public void testsSimpleCreateValidationErrorWithEmptyName() throws Exception {
    Item savedItem = TestHelpers.createItem();
    savedItem.setName("");
    Mockito.when(itemRepository.save(savedItem)).thenReturn(savedItem);
    try {
      itemService.save(savedItem);
    } catch (BusinessRulesException exception) {
      assertEquals("BusinessRulesError is thrown with the correct error message", "Name is a required field;", exception.getErrorMessage());
    }

  }

  @Test
  public void testsSimpleCreateValidationErrorWithNameFieldMissing() throws Exception {
    Item savedItem = TestHelpers.createItem();
    savedItem.setName(null);
    Mockito.when(itemRepository.save(savedItem)).thenReturn(savedItem);
    try {
      itemService.save(savedItem);
    } catch (BusinessRulesException exception) {
      assertEquals("BusinessRulesError is thrown with the correct error message", "Name is a required field;", exception.getErrorMessage());
    }
  }

  @Test
  public void testSimpleCreateValidationErrorWithNumberInName() throws Exception {
    Item savedItem = TestHelpers.createItem();
    savedItem.setName("Angus Burger 1");
    Mockito.when(itemRepository.save(savedItem)).thenReturn(savedItem);
    try {
      itemService.save(savedItem);
    } catch (BusinessRulesException exception) {
      assertEquals("BusinessRulesError is thrown with the correct error message", "Name can only contain letters A-Z;",
                   exception.getErrorMessage());
    }
  }

  @Test
  public void testSimpleCreateValidationErrorWithPriceFieldMissing() throws Exception {
    Item savedItem = TestHelpers.createItem();
    savedItem.setPrice(null);
    Mockito.when(itemRepository.save(savedItem)).thenReturn(savedItem);
    try {
      itemService.save(savedItem);
    } catch (BusinessRulesException exception) {
      assertEquals("BusinessRulesError is thrown with the correct error message", "Price is a required field;",
                   exception.getErrorMessage());
    }
  }

  @Test
  public void testSimpleCreateValidationErrorWithPriceHavingThreeDecimalPlaces() throws Exception {
    Item savedItem = TestHelpers.createItem();
    savedItem.setPrice(new BigDecimal("3.999"));
    Mockito.when(itemRepository.save(savedItem)).thenReturn(savedItem);
    try {
      itemService.save(savedItem);
    } catch (BusinessRulesException exception) {
      assertEquals("BusinessRulesError is thrown with the correct error message",
                   "Price must be a number less than 100 with two decimal places;", exception.getErrorMessage());
    }
  }

  @Test
  public void testSimpleCreateValidationErrorWithPriceHavingThreeIntegerDigits() throws Exception {
    Item savedItem = TestHelpers.createItem();
    savedItem.setPrice(new BigDecimal("100.00"));
    Mockito.when(itemRepository.save(savedItem)).thenReturn(savedItem);
    try {
      itemService.save(savedItem);
    } catch (BusinessRulesException exception) {
      assertEquals("BusinessRulesError is thrown with the correct error message",
                   "Price must be a number less than 100 with two decimal places;", exception.getErrorMessage());
    }
  }

  @Test
  public void testSimpleCreateValidationErrorWithPriceLessThanZero() throws Exception {
    Item savedItem = TestHelpers.createItem();
    savedItem.setPrice(new BigDecimal("-1.00"));
    Mockito.when(itemRepository.save(savedItem)).thenReturn(savedItem);
    try {
      itemService.save(savedItem);
    } catch (BusinessRulesException exception) {
      assertEquals("BusinessRulesError is thrown with the correct error message", "Price must be greater or equal to 0;",
                   exception.getErrorMessage());
    }
  }

  @Test
  public void testSimpleCreateValidationMultipleErrorForPriceAndNameError() throws Exception {
    Item savedItem = TestHelpers.createItem();
    savedItem.setPrice(new BigDecimal("-1.00"));
    savedItem.setName("Angus Burger 1");
    Mockito.when(itemRepository.save(savedItem)).thenReturn(savedItem);
    try {
      itemService.save(savedItem);
    } catch (BusinessRulesException exception) {
      assertTrue("BusinessRulesError is thrown with the correct error message",
                 exception.getErrorMessage().contains("Price must be greater or equal to 0") && exception.getErrorMessage()
                     .contains(
                         "Name can only contain letters A-Z"));//used contains so it less brittle in case the order of validation items changes
    }
  }

}