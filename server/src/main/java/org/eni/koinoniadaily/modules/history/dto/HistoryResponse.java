package org.eni.koinoniadaily.modules.history.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HistoryResponse {
  
  private Long id;

  private Long teachingId;

  private String teachingTitle;

  private String teachingThumbnailUrl;

  private LocalDateTime teachingTaughtAt;

  private boolean isMarkedRead;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
