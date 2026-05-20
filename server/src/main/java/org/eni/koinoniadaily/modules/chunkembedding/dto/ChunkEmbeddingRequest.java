package org.eni.koinoniadaily.modules.chunkembedding.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChunkEmbeddingRequest {

  @NotEmpty(message = "Array of teachingId is required")
  private List<Long> teachingIds;
}
