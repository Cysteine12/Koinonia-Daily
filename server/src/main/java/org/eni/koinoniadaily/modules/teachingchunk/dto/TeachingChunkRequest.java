package org.eni.koinoniadaily.modules.teachingchunk.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TeachingChunkRequest {

  @NotNull(message = "teachingId is required")
  @Positive(message = "teachingId must be greater than zero")
  private Long teachingId;
}
