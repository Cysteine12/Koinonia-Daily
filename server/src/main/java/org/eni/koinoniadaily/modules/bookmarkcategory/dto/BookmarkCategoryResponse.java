package org.eni.koinoniadaily.modules.bookmarkcategory.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookmarkCategoryResponse {
  
  private Long id;
  
  private String name;

  private Integer totalBookmarks;

  private Instant createdAt;

  private Instant updatedAt;
}
