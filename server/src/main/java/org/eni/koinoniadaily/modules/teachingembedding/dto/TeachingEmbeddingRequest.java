package org.eni.koinoniadaily.modules.teachingembedding.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeachingEmbeddingRequest {

  @NotNull(message = "teachingId is required")
  @Positive(message = "teachingId must be greater than zero")
  private Long teachingId;
}
