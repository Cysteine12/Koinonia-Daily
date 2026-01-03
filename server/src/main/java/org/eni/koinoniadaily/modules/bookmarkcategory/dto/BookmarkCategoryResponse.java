package org.eni.koinoniadaily.modules.bookmarkcategory.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BookmarkCategoryResponse {
  
  private Long id;
  
  private String name;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
