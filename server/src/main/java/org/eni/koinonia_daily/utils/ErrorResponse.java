package org.eni.koinonia_daily.utils;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ErrorResponse extends ApiResponse {
  
  private final int status;
  
  private final String error;
  
  private final String path;
  
  private final LocalDateTime timestamp;
}
