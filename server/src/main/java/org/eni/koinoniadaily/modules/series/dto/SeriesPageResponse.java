package org.eni.koinoniadaily.modules.series.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SeriesPageResponse {
  
  private Long id;

  private String title;

  private String description;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Long totalTeachings;
}
