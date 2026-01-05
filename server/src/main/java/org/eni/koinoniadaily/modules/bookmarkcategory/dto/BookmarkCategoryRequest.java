package org.eni.koinoniadaily.modules.bookmarkcategory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkCategoryRequest {
  
  @NotBlank
  private String name;
}
