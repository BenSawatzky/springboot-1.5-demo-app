package answer.king.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import answer.king.exception.BusinessRulesException;


public class ValidationUtils<T> {

  public void validateParams(T t, Validator validator) throws BusinessRulesException {
    Set<ConstraintViolation<T>> violations = validator.validate(t);
    if (violations != null && violations.size() > 0) {
      StringBuilder stringBuilder = new StringBuilder();
      for (ConstraintViolation<T> violation : violations) {
        stringBuilder.append(violation.getMessage());
        stringBuilder.append(";");
      }
      throw new BusinessRulesException(stringBuilder.toString());
    }
  }


}
