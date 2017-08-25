package answer.king.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import answer.king.exception.BusinessRulesException;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

  public static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  public static final String VALIDATION_ERROR = "Validation Error";
  public static final String PARSING_ERROR = "Parsing Error";
  public static final String UNKNOWN_ERROR = "Unknown Error";
  public static final String UNKNOWN_ERROR_MESSAGE = "System administrator will be alerted to help with your issue";

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
    StringBuilder errorMessage = new StringBuilder();
    if (ex.getBindingResult().hasErrors()) {
      for (FieldError error : ex.getBindingResult().getFieldErrors()) {
        errorMessage.append(error.getDefaultMessage()).append(";");
      }
    }
    return new ResponseEntity<>(new ErrorResponse(VALIDATION_ERROR, errorMessage.toString()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
    return new ResponseEntity<>(new ErrorResponse(PARSING_ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BusinessRulesException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleBusinessRulesException(final BusinessRulesException ex) {
    return new ResponseEntity<>(new ErrorResponse(ex.getCode(), ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(IllegalStateException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleBusinessRulesException(final IllegalStateException ex) {
    if (ex.getCause() instanceof BusinessRulesException) {
      return new ResponseEntity<>(
          new ErrorResponse(((BusinessRulesException) ex.getCause()).getCode(), ((BusinessRulesException) ex.getCause()).getErrorMessage()),
          HttpStatus.BAD_REQUEST);
    } else

    {
      return handleUnknownException(ex);
    }

  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleUnknownException(Exception ex) {
    //Prevent embarrassing error messages for anything not caught in App
    LOG.error("Uncaught Error Occurred: ", ex);
    return new ResponseEntity<>(
        new ErrorResponse(UNKNOWN_ERROR, UNKNOWN_ERROR_MESSAGE),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static class ErrorResponse {

    private final String code;

    public String getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

    private final String message;

    public ErrorResponse(String code, String message) {
      this.code = code;
      this.message = message;
    }
  }
}
