package org.eni.koinoniadaily.modules.search.projection;

import org.eni.koinoniadaily.modules.teaching.TeachingType;

import java.time.Instant;

public interface SearchResult {

  Long getTeachingId();

  Long getChunkId();

  String getTeachingTitle();

  String getSectionTitle();

  String getContent();

  int getChunkIndex();

  String getThumbnailUrl();

  TeachingType getType();

  Instant getTaughtAt();

  double getScore();

  String getMatchSources();
}
