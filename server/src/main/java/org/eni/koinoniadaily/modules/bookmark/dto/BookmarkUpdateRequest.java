package org.eni.koinoniadaily.modules.bookmark.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkUpdateRequest {
  
  @Size(max = 500, message = "Note length cannot exceed 500 characters")
  private String note;
}
