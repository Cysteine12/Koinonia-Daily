package org.eni.koinoniadaily.utils;

import java.time.Instant;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ErrorResponse extends ApiResponse {
  
  private final int status;
  
  private final String error;
  
  private final String path;

  private final String code;

  private final Object errors;

  private final Instant timestamp;
}
