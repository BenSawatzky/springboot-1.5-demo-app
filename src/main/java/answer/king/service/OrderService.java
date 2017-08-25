package answer.king.service;

import java.math.BigDecimal;
import java.util.List;

import answer.king.exception.BusinessRulesException;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;

public interface OrderService {

  List<Order> getAll();

  Order save(Order order);

  LineItem addItem(Long id, LineItem lineItem);

  Receipt pay(Long id, BigDecimal payment) throws BusinessRulesException;
}
