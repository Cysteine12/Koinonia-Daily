package org.eni.koinoniadaily.modules.teaching.dto;

import java.time.Instant;

import org.eni.koinoniadaily.modules.series.dto.SeriesSummary;
import org.eni.koinoniadaily.modules.teaching.EmbeddingStatus;
import org.eni.koinoniadaily.modules.teaching.TeachingStatus;
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
  
  private SeriesSummary series;

  private Integer seriesPart;

  private TeachingStatus status;

  private EmbeddingStatus embeddingStatus;

  private Instant taughtAt;
  
  private Instant createdAt;
  
  private Instant updatedAt;
}
