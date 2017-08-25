package answer.king.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

import answer.king.exception.BusinessRulesException;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.service.OrderService;
import answer.king.service.ReceiptService;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

  @Autowired
  private ReceiptService receiptService;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public Receipt getById(@PathVariable("id") Long id) {
    return receiptService.getById(id);
  }

}
