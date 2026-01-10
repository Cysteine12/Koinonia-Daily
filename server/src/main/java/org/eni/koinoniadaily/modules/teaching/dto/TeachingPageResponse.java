package org.eni.koinoniadaily.modules.teaching.dto;

import java.time.LocalDateTime;

import org.eni.koinoniadaily.modules.teaching.TeachingType;

public interface TeachingPageResponse {
  
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
