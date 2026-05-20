package org.eni.koinoniadaily.modules.chunkembedding.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChunkEmbeddingRequest {

  @NotEmpty(message = "Array of teachingId is required")
  private List<@NotNull(message = "teachingId is required")
      @Positive(message = "teachingId must be positive") Long> teachingIds;
}
