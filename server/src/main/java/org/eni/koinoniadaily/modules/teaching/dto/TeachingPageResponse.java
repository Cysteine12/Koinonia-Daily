package org.eni.koinoniadaily.modules.teaching.dto;

import java.time.Instant;

import org.eni.koinoniadaily.modules.teaching.TeachingType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeachingPageResponse {
  
  private Long id;

  private String title;
  
  private String scripturalReferences;

  private String summary;

  private String audioUrl;

  private String videoUrl;

  private String thumbnailUrl;

  private TeachingType type;

  private String tags;

  private Integer seriesPart;

  private Instant taughtAt;
  
  private Instant createdAt;
  
  private Instant updatedAt;
}
