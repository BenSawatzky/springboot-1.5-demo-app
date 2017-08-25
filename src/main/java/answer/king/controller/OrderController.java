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
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

  @Autowired
  private OrderService orderService;

  @RequestMapping(method = RequestMethod.GET)
  public List<Order> getAll() {
    return orderService.getAll();
  }

  @RequestMapping(method = RequestMethod.POST)
  public Order create() {
    return orderService.save(new Order());
  }

  @RequestMapping(value = "/{id}/lineitem", method = RequestMethod.POST)
  public LineItem addLineItem(@PathVariable("id") Long id, @RequestBody LineItem lineItem) {
    return orderService.addItem(id, lineItem);
  }

  @RequestMapping(value = "/{id}/pay", method = RequestMethod.PUT)
  public Receipt pay(@PathVariable("id") Long id, @RequestBody BigDecimal payment) throws BusinessRulesException {
    return orderService.pay(id, payment);
  }
}
