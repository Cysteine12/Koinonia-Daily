package org.eni.koinoniadaily.modules.teaching.dto;

import java.time.LocalDateTime;

import org.eni.koinoniadaily.modules.series.Series;
import org.eni.koinoniadaily.modules.teaching.TeachingType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeachingResponse {
  
  private Long id;

  private String title;
  
  private String scripturalReferences;

  private String message;

  private String summary;

  private String audioUrl;

  private String videoUrl;

  private String thumbnailUrl;

  private TeachingType type;

  private String tags;
  
  private Series series;

  private int seriesPart;

  private LocalDateTime taughtAt;
  
  private LocalDateTime createdAt;
  
  private LocalDateTime updatedAt;
}
