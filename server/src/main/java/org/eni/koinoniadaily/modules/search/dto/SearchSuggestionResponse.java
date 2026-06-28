package org.eni.koinoniadaily.modules.search.dto;

import lombok.Builder;
import lombok.Getter;
import org.eni.koinoniadaily.modules.teaching.TeachingType;

import java.time.Instant;

@Getter
@Builder
public class SearchSuggestionResponse{

  private Long id;

  private String title;

  private String thumbnailUrl;

  private TeachingType type;

  private Instant taughtAt;
}
