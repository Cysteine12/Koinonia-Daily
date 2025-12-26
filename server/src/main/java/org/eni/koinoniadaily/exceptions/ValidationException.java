package org.eni.koinoniadaily.exceptions;

public class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
  }  
}
