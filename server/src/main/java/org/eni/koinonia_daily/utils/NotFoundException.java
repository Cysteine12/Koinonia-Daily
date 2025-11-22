package org.eni.koinonia_daily.utils;

import java.util.NoSuchElementException;

public class NotFoundException extends NoSuchElementException {
  public NotFoundException(String message) {
    super(message);
  }
}
