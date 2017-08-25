package answer.king.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T_LINE_ITEM")
public class LineItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne(targetEntity = Item.class, fetch = FetchType.EAGER)
  private Item item;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ORDER_ID")
  private Order order;

  private Integer quantity;

  private BigDecimal price;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Integer getQuantity() {
    return quantity == null || quantity == 0 ? 1 : quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LineItem)) {
      return false;
    }

    LineItem lineItem = (LineItem) o;

    if (getId() != null ? !getId().equals(lineItem.getId()) : lineItem.getId() != null) {
      return false;
    }
    if (getItem() != null ? !getItem().equals(lineItem.getItem()) : lineItem.getItem() != null) {
      return false;
    }
    if (getOrder() != null ? !getOrder().equals(lineItem.getOrder()) : lineItem.getOrder() != null) {
      return false;
    }
    if (getQuantity() != null ? !getQuantity().equals(lineItem.getQuantity()) : lineItem.getQuantity() != null) {
      return false;
    }
    if (getPrice() != null ? !getPrice().equals(lineItem.getPrice()) : lineItem.getPrice() != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getItem() != null ? getItem().hashCode() : 0);
    result = 31 * result + (getOrder() != null ? getOrder().hashCode() : 0);
    result = 31 * result + (getQuantity() != null ? getQuantity().hashCode() : 0);
    result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
    return result;
  }
}
