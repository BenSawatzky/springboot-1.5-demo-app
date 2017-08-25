package answer.king.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "T_ORDER")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Boolean paid = false;

  @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL, CascadeType.PERSIST})
  private List<LineItem> lineItems;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getPaid() {
    return paid;
  }

  public void setPaid(Boolean paid) {
    this.paid = paid;
  }

  public List<LineItem> getLineItems() {
    return lineItems == null ? new ArrayList<>() : lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

  public void addLineItem(LineItem lineItem) {
    lineItems = lineItems == null ? new ArrayList<>() : lineItems;
    lineItems.add(lineItem);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Order)) {
      return false;
    }

    Order order = (Order) o;

    if (getId() != null ? !getId().equals(order.getId()) : order.getId() != null) {
      return false;
    }
    if (getPaid() != null ? !getPaid().equals(order.getPaid()) : order.getPaid() != null) {
      return false;
    }
    return !(getLineItems() != null ? !getLineItems().equals(order.getLineItems()) : order.getLineItems() != null);

  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getPaid() != null ? getPaid().hashCode() : 0);
    result = 31 * result + (getLineItems() != null ? getLineItems().hashCode() : 0);
    return result;
  }
}
