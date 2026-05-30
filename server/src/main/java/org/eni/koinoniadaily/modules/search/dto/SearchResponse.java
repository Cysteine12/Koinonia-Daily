package org.eni.koinoniadaily.modules.search.dto;

import lombok.Builder;
import lombok.Getter;
import org.eni.koinoniadaily.modules.teaching.TeachingType;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class SearchResponse{

  private Long teachingId;

  private Long chunkId;

  private String teachingTitle;

  private String sectionTitle;

  private String content;

  private int chunkIndex;

  private String thumbnailUrl;

  private TeachingType type;

  private Instant taughtAt;

  private double score;

  private List<String> matchSources;
}
