package org.eni.koinoniadaily.modules.series.dto;

import java.time.Instant;
import java.util.List;

import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeriesResponse {
  
  private Long id;

  private String name;

  private String description;

  private String thumbnailUrl;

  private Instant createdAt;

  private Instant updatedAt;

  private List<TeachingWithoutMessageProjection> teachings;
}
