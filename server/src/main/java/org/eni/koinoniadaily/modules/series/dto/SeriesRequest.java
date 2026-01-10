package org.eni.koinoniadaily.modules.series.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeriesRequest {
  
  @NotBlank(message = "Title is required")
  @Size(max = 100, message = "Title length cannot exceed 50 characters")
  private String title;

  @NotBlank(message = "Description is required")
  @Size(max = 255, message = "Description length cannot exceed 255 characters")
  private String description;
}
