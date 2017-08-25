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

import answer.king.exception.BusinessRulesException;
import answer.king.helpers.TestHelpers;
import answer.king.model.Receipt;
import answer.king.repo.ReceiptRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

@RunWith(SpringRunner.class)
public class ReceiptServiceTest {

  @TestConfiguration
  static class OrderServiceImplTestContextConfiguration {

    @Bean
    public ReceiptService receiptService() {
      return new ReceiptServiceImpl();
    }
  }

  @Autowired
  ReceiptService receiptService;

  @MockBean
  private ReceiptRepository receiptRepository;

  @Test
  public void testReceiptGetById() throws Exception, BusinessRulesException {
    Receipt receipt = TestHelpers.createReceipt();
    receipt.setPayment(new BigDecimal("8.00"));
    Mockito.when(receiptRepository.findOne(any(Long.class))).thenReturn(receipt);
    Receipt returnedReceipt = receiptService.getById(TestHelpers.UNIT_TEST_ID);
    assertEquals("Receipt returned from repository is equal to the Mock", receipt.getId(), returnedReceipt.getId());
  }
}