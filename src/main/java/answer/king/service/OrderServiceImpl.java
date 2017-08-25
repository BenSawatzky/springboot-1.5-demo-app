package answer.king.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Validator;

import answer.king.exception.BusinessRulesException;
import answer.king.model.LineItem;
import answer.king.model.Order;
import answer.king.model.Receipt;
import answer.king.repo.LineItemRepository;
import answer.king.repo.OrderRepository;
import answer.king.repo.ReceiptRepository;
import answer.king.validation.ValidationUtils;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private LineItemRepository lineItemRepository;
  @Autowired
  private ReceiptRepository receiptRepository;
  @Autowired
  private Validator validator;
  private ValidationUtils<Receipt> validationUtils = new ValidationUtils<>();


  @Override
  public List<Order> getAll() {
    return orderRepository.findAll();
  }

  @Override
  public Order save(Order order) {
    return orderRepository.save(order);
  }

  @Override
  public LineItem addItem(Long id, LineItem lineItem) {
    Order order = orderRepository.findOne(id);
    Long itemId = lineItem.getItem().getId();
    //playing about with streams
    Map<Boolean, List<LineItem>> partionedList = order.getLineItems().stream()
        .collect(Collectors.partitioningBy(s -> s.getItem().getId() == itemId));
  //playing about with streams
    order.setLineItems(partionedList.get(false));
    if (partionedList.get(true).size() == 0) {
      lineItem.setPrice(lineItem.getItem().getPrice());
      lineItem = lineItemRepository.save(lineItem);
      lineItem.setOrder(order);
    } else if (partionedList.get(true).size() == 1) {
      partionedList.get(true).get(0).setQuantity(partionedList.get(true).get(0).getQuantity() + lineItem
          .getQuantity()); //resusing LineItemId means when order save it will automatically update
      lineItem = partionedList.get(true).get(0);
    } else {
      //Should never get here but coding against it. Possibly throw an exception ??
      lineItem.setQuantity(partionedList.get(true).stream().collect(Collectors.summingInt(LineItem::getQuantity)) + lineItem.getQuantity());
      partionedList.get(true).stream().forEach(deleteLineItem -> lineItemRepository.delete(deleteLineItem.getId()));
      lineItem.setPrice(lineItem.getItem().getPrice());
      lineItem = lineItemRepository.save(lineItem);
    }
    order.addLineItem(lineItem);
    orderRepository.save(order);
    return lineItem;
  }

  @Override
  public Receipt pay(Long id, BigDecimal payment) throws BusinessRulesException {
    Order order = orderRepository.findOne(id);
    //TODO: If already paid throw an exception
    Receipt receipt = new Receipt();
    receipt.setPayment(payment);
    receipt.setOrder(order);
    validationUtils.validateParams(receipt, validator);
    order.setPaid(true);
    //save the fact it's paid back to the database
    orderRepository.save(order);
    receiptRepository.save(receipt);
    return receipt;
  }
}
