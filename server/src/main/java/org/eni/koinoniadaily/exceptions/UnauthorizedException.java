package org.eni.koinoniadaily.exceptions;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
  
  private final String code;

  /**
   * Creates an UnauthorizedException with the specified detail message and the error code "UNAUTHORIZED".
   *
   * @param message the detail message describing the authorization failure
   */
  public UnauthorizedException(String message) {
    super(message);
    this.code = "UNAUTHORIZED";
  }

  /**
   * Creates an UnauthorizedException with a specific error code and message.
   *
   * @param code    application-specific error code identifying the authorization failure
   * @param message human-readable description of the failure
   */
  public UnauthorizedException(String code, String message) {
    super(message);
    this.code = code;
  }
  
  /**
   * Creates an UnauthorizedException with the given detail message and cause, and sets the error code to "UNAUTHORIZED".
   *
   * @param message the detail message for the exception
   * @param cause   the cause of the exception
   */
  public UnauthorizedException(String message, Throwable cause) {
    super(message, cause);
    this.code = "UNAUTHORIZED";
  }
}
