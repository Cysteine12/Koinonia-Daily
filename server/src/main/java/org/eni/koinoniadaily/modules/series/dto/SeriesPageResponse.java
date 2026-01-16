package org.eni.koinoniadaily.modules.series.dto;

import java.time.LocalDateTime;

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

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Integer totalTeachings;
}
