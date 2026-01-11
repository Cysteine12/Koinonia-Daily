package org.eni.koinoniadaily.modules.teaching.projection;

import java.time.LocalDateTime;

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

  int getSeriesPart();

  LocalDateTime getTaughtAt();
  
  LocalDateTime getCreatedAt();
  
  LocalDateTime getUpdatedAt();
}
