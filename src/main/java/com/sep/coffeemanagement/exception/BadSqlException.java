package com.sep.coffeemanagement.exception;

public class BadSqlException extends RuntimeException {

  public BadSqlException(String message) {
    super(message);
  }
}
