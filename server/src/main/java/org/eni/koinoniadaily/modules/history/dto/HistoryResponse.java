package org.eni.koinoniadaily.modules.history.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HistoryResponse {
  
  private Long id;

  private Long teachingId;

  private String teachingTitle;

  private String teachingThumbnailUrl;

  private Instant teachingTaughtAt;

  private boolean isMarkedRead;

  private Instant createdAt;

  private Instant updatedAt;
}
