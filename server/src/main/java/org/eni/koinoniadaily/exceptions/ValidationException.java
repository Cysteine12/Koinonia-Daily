package org.eni.koinoniadaily.exceptions;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

  private String code = "VALIDATION_ERROR";

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(String code, String message) {
    super(message);
    this.code = code;
  }
}
