package org.eni.koinonia_daily.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
  private int status;
  private String message;
  private String error;
  private String path;
  private LocalDateTime timestamp;
}
