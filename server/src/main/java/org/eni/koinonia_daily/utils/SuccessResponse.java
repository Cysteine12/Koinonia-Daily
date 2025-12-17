package org.eni.koinonia_daily.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> extends ApiResponse {

  private final T data;

  protected SuccessResponse(T data, String message) {
    super(true, message);
    this.data = data;
  }

  public static <T> SuccessResponse<T> of(T data, String message) {
    return new SuccessResponse<>(data, message);
  }

  public static <T> SuccessResponse<T> data(T data) {
    return new SuccessResponse<>(data, null);
  }

  public static SuccessResponse<Void> message(String message) {
    return new SuccessResponse<>(null, message);
  }

  public static SuccessResponse<Void> empty() {
    return new SuccessResponse<>(null, null);
  }
  
}
