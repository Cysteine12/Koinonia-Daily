package org.eni.koinoniadaily.modules.series.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.eni.koinoniadaily.modules.teaching.dto.TeachingPageResponse;

public interface SeriesResponse {
  
  Long getId();

  String getTitle();

  String getDescription();

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();

  List<TeachingPageResponse> getTeachings();
}
