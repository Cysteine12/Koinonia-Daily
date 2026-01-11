package org.eni.koinoniadaily.modules.collection.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CollectionResponse {
  
  private Long id;

  private String name;

  private String description;

  private String thumbnailUrl;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private List<TeachingWithoutMessageProjection> teachings;
}
