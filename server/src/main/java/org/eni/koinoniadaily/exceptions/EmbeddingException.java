package org.eni.koinoniadaily.exceptions;

import lombok.Getter;

public class EmbeddingException extends RuntimeException {

  public EmbeddingException(String message, Throwable cause) {
    super(message, cause);
  }
}
