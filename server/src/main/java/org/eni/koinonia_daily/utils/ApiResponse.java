package org.eni.koinonia_daily.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public abstract class ApiResponse {

  private final boolean success;

  private final String message;
}
