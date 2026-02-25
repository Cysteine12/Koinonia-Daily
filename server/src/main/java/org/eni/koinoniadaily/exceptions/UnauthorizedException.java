package org.eni.koinoniadaily.exceptions;

public class UnauthorizedException extends RuntimeException {
  
  private final String code;

  public UnauthorizedException(String message) {
    super(message);
    this.code = "UNAUTHORIZED";
  }

  public UnauthorizedException(String code, String message) {
    super(message);
    this.code = code;
  }
  
  public UnauthorizedException(String message, Throwable cause) {
    super(message, cause);
    this.code = "UNAUTHORIZED";
  }
}
