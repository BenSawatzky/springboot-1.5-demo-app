package answer.king.exception;


public class BusinessRulesException extends AnswerKingCaughtException {

  private String errorMessage;

  public BusinessRulesException(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public static final String ERROR_CODE = "VALIDATION_ERROR";

  @Override
  public String getCode() {
    return ERROR_CODE;
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }
}
