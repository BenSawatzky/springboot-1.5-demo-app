package answer.king.validation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import answer.king.model.Receipt;


@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PaymentGreaterThanDue.Validator.class)
public @interface PaymentGreaterThanDue {

  String message() default "The payment given is less than the amount required to be paid";

  Class<?>[] groups() default {};

  Class<?>[] payload() default {};

  @Component
  class Validator implements ConstraintValidator<PaymentGreaterThanDue, Receipt> {

    @Override
    public void initialize(PaymentGreaterThanDue isInternalAndValid) {
    }

    @Override
    public boolean isValid(Receipt receipt, ConstraintValidatorContext context) {
      return receipt.getChange().compareTo(BigDecimal.ZERO) == 1 || receipt.getChange().compareTo(BigDecimal.ZERO) == 0;
    }
  }
}
