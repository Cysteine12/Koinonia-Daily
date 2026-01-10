package org.eni.koinoniadaily.modules.series.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.eni.koinoniadaily.modules.teaching.dto.TeachingPageResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeriesResponse {
  
  private Long id;

  private String title;

  private String description;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private List<TeachingPageResponse> teachings;
}
