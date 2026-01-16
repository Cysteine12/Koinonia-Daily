package org.eni.koinoniadaily.modules.bookmarkcategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkCategoryRequest {
  
  @NotBlank(message = "Name is required")
  @Size(message = "Name length cannot exceed 50 characters")
  private String name;
}
