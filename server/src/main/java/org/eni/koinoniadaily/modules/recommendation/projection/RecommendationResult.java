package org.eni.koinoniadaily.modules.recommendation.projection;

import org.eni.koinoniadaily.modules.teaching.TeachingType;

import java.time.Instant;

public interface RecommendationResult {

  Long getId();

  String getTitle();

  String getSummary();

  String getThumbnailUrl();

  TeachingType getType();

  Integer getSeriesPart();

  Instant getTaughtAt();

  Instant getCreatedAt();

  Instant getUpdatedAt();

  double getScore();
}
