package org.eni.koinoniadaily.modules.history.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryRequest {

  @NotNull(message = "Marked as read is required")
  private Boolean isMarkedRead;
}
