package answer.king.model;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import answer.king.validation.PaymentGreaterThanDue;

@PaymentGreaterThanDue
@Entity
@Table(name = "T_RECEIPT")
public class Receipt {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private BigDecimal payment;

  @OneToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
  private Order order;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public BigDecimal getPayment() {
    return payment;
  }

  public void setPayment(BigDecimal payment) {
    this.payment = payment;
  }

  public BigDecimal getChange() {
    Function<LineItem, BigDecimal> totalMapper = lineItem -> lineItem.getPrice().multiply(new BigDecimal(lineItem.getQuantity()));
    BigDecimal totalOrderPrice = order.getLineItems()
        .stream()
        .map(totalMapper)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return payment.subtract(totalOrderPrice);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Receipt)) {
      return false;
    }

    Receipt receipt = (Receipt) o;

    if (getPayment() != null ? !getPayment().equals(receipt.getPayment()) : receipt.getPayment() != null) {
      return false;
    }
    return !(getOrder() != null ? !getOrder().equals(receipt.getOrder()) : receipt.getOrder() != null);

  }

  @Override
  public int hashCode() {
    int result = getPayment() != null ? getPayment().hashCode() : 0;
    result = 31 * result + (getOrder() != null ? getOrder().hashCode() : 0);
    return result;
  }
}
