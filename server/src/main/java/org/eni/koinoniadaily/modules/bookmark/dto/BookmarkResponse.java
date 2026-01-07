package org.eni.koinoniadaily.modules.bookmark.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookmarkResponse {
  
  private Long id;

  private Long teachingId;

  private String teachingTitle;

  private String teachingThumbnailUrl;

  private LocalDateTime teachingTaughtAt;

  private String note;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
