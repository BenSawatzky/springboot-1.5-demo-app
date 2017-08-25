package answer.king.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "T_ITEM")
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotEmpty(message = "Name is a required field")
  @Pattern(regexp = "^[A-Z a-z]*$", message = "Name can only contain letters A-Z")
  private String name;

  @NotNull(message = "Price is a required field")
  @Digits(message = "Price must be a number less than 100 with two decimal places", integer = 2, fraction = 2)
  @Min(message = "Price must be greater or equal to 0", value = 0)
  private BigDecimal price;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
    if (!(o instanceof Item)) {
      return false;
    }

    Item item = (Item) o;

    if (getId() != null ? !getId().equals(item.getId()) : item.getId() != null) {
      return false;
    }
    if (getName() != null ? !getName().equals(item.getName()) : item.getName() != null) {
      return false;
    }
    return !(getPrice() != null ? !getPrice().equals(item.getPrice()) : item.getPrice() != null);

  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
    return result;
  }
}
