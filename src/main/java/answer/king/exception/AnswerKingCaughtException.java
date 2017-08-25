package answer.king.exception;


public abstract class AnswerKingCaughtException extends Throwable {

  public abstract String getCode();

  public abstract String getErrorMessage();

}
