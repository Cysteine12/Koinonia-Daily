package org.eni.koinoniadaily.exceptions;

public class EmailSendingException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs an EmailSendingException with the specified detail message.
   *
   * @param message the detail message describing the email-sending failure
   */
  public EmailSendingException(String message) {
    super(message);
  }

  /**
   * Constructs an EmailSendingException with the specified detail message and cause.
   *
   * @param message the detail message describing the email sending failure
   * @param cause the underlying cause of this exception, or {@code null} if none
   */
  public EmailSendingException(String message, Throwable cause) {
    super(message, cause);
  }
}