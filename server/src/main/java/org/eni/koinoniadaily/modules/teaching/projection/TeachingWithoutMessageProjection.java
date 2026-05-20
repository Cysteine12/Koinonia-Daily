package org.eni.koinoniadaily.modules.teaching.projection;

import java.time.Instant;

import org.eni.koinoniadaily.modules.teaching.TeachingStatus;
import org.eni.koinoniadaily.modules.teaching.TeachingType;

public interface TeachingWithoutMessageProjection {
  
  Long getId();

  String getTitle();
  
  String getScripturalReferences();

  String getSummary();

  String getAudioUrl();

  String getVideoUrl();

  String getThumbnailUrl();

  TeachingType getType();

  String getTags();

  Integer getSeriesPart();

  TeachingStatus getStatus();

  Instant getTaughtAt();
  
  Instant getCreatedAt();
  
  Instant getUpdatedAt();
}
