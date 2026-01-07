package org.eni.koinoniadaily.modules.bookmark.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkRequest {
  
  @NotNull(message = "teachingId is required")
  @Positive(message = "teachingId must be greater than zero")
  private Long teachingId;

  @NotNull(message = "categoryId is required")
  @Positive(message = "categoryId must be greater than zero")
  private Long categoryId;

  private String note;
}
