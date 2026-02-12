package org.eni.koinoniadaily.exceptions;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

  private final String code;

  public ValidationException(String message) {
    super(message);
    this.code = "VALIDATION_ERROR";
  }

  public ValidationException(String code, String message) {
    super(message);
    this.code = code;
  }
}
