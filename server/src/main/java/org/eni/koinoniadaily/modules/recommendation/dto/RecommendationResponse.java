package org.eni.koinoniadaily.modules.recommendation.dto;

import lombok.Builder;
import lombok.Getter;
import org.eni.koinoniadaily.modules.recommendation.RecommendationSignal;
import org.eni.koinoniadaily.modules.teaching.TeachingType;

import java.time.Instant;

@Getter
@Builder
public class RecommendationResponse {

  private Long id;

  private String title;

  private String summary;

  private String thumbnailUrl;

  private TeachingType type;

  private Integer seriesPart;

  private Instant taughtAt;

  private Instant createdAt;

  private Instant updatedAt;

  private RecommendationSignal signal;
}
