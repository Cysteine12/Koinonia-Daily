package org.eni.koinoniadaily.modules.collection.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CollectionPageResponse {
  
  private Long id;
  
  private String name;

  private String description;

  private String thumbnailUrl;
  
  private Instant createdAt;
  
  private Instant updatedAt;

  private Integer totalTeachings;
}
