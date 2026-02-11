package org.eni.koinoniadaily.modules.series.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SeriesPageResponse {
  
  private Long id;

  private String name;

  private String description;

  private String thumbnailUrl;

  private Instant createdAt;

  private Instant updatedAt;

  private Integer totalTeachings;
}
