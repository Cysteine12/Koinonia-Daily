package org.eni.koinoniadaily.modules.collection.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionTeachingRequest {
  
  @NotNull(message = "Teaching id is required")
  @Positive(message = "Teaching id must be greater than zero")
  private Long teachingId;
}
