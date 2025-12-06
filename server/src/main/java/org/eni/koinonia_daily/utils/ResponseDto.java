package org.eni.koinonia_daily.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter @AllArgsConstructor
public class ResponseDto {

  private String message;

  private Object data;
}
