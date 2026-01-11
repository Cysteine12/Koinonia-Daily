package org.eni.koinoniadaily.modules.collection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionRequest {
  
  @NotBlank(message = "Name is required")
  @Size(max = 50, message = "Name length cannot exceed 50 characters")
  private String name;

  @NotBlank(message = "Description is required")
  @Size(max = 255, message = "Description length cannot exceed 255 characters")
  private String description;

  @NotBlank(message = "Thumbnail URL is required")
  @Size(max = 100, message = "Thumbnail URL length cannot exceed 100 characters")
  private String thumbnailUrl;
}
