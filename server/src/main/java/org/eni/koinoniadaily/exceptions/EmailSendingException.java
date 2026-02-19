package org.eni.koinoniadaily.exceptions;

public class EmailSendingException extends RuntimeException {

  private static final long serialVersionID = 1L;

  public EmailSendingException(String message) {
    super(message);
  }

  public EmailSendingException(String message, Throwable cause) {
    super(message, cause);
  }
}
