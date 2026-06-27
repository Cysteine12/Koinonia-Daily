package org.eni.koinoniadaily.modules.teaching.projection;

import java.time.Instant;

import org.eni.koinoniadaily.modules.teaching.TeachingType;

public interface TeachingTitleSuggestionProjection {
  
  Long getId();

  String getTitle();
  
  String getThumbnailUrl();

  TeachingType getType();

  Instant getTaughtAt();
}
