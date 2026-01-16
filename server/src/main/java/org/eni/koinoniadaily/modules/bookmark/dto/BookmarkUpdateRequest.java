package org.eni.koinoniadaily.modules.bookmark.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookmarkUpdateRequest {
  
  @Size(message = "Note cannot be more than 500 characters")
  private String note;
}
