package com.sep.coffeemanagement.exception;

import com.sep.coffeemanagement.dto.common.CommonResponse;
import com.sep.coffeemanagement.log.AppLogger;
import com.sep.coffeemanagement.log.LoggerFactory;
import com.sep.coffeemanagement.log.LoggerType;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
  private static final AppLogger APP_LOGGER = LoggerFactory.getLogger(
    LoggerType.APPLICATION
  );

  @ExceptionHandler(BadSqlException.class)
  public ResponseEntity<CommonResponse<String>> handleBadSqlException(BadSqlException e) {
    APP_LOGGER.error(e.getMessage());
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        false,
        "",
        e.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<CommonResponse<String>> handleForbiddenException(
    ForbiddenException e
  ) {
    APP_LOGGER.error(e.getMessage());
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(false, "", e.getMessage(), HttpStatus.FORBIDDEN.value()),
      null,
      HttpStatus.OK.value()
    );
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<CommonResponse<String>> handleNotFound(
    ResourceNotFoundException e
  ) {
    APP_LOGGER.error(e.getMessage());
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(false, "", e.getMessage(), HttpStatus.NOT_FOUND.value()),
      null,
      HttpStatus.OK.value()
    );
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<CommonResponse<String>> handleUnAuthorizedException(
    UnauthorizedException e
  ) {
    APP_LOGGER.error(e.getMessage());
    return new ResponseEntity<CommonResponse<String>>(
      new CommonResponse<String>(
        false,
        null,
        e.getMessage(),
        HttpStatus.UNAUTHORIZED.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<CommonResponse<Map<String, String>>> handleInvalidRequestException(
    InvalidRequestException e
  ) {
    APP_LOGGER.error(e.getMessage());
    return new ResponseEntity<>(
      new CommonResponse<Map<String, String>>(
        false,
        e.getResult(),
        e.getMessage(),
        HttpStatus.BAD_REQUEST.value()
      ),
      null,
      HttpStatus.OK.value()
    );
  }
}
