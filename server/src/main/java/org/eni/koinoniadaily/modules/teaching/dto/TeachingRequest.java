package org.eni.koinoniadaily.modules.teaching.dto;

import java.time.LocalDateTime;

import org.eni.koinoniadaily.modules.teaching.TeachingType;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
public class TeachingRequest {

  @NotBlank(message = "Title is required")
  private String title;
  
  @NotBlank(message = "Scriptural references are required (semi-colon-separated)")
  private String scripturalReferences;
  
  @NotBlank(message = "Message is required")
  private String message;
  
  @NotBlank(message = "Summary is required")
  private String summary;
  
  @NotBlank(message = "Audio URL is required")
  @URL(message = "Audio URL must be a valid URL")
  private String audioUrl;
  
  @NotBlank(message = "Video URL is required")
  @URL(message = "Video URL must be a valid URL")
  private String videoUrl;
  
  @NotBlank(message = "Thumbnail is required")
  private String thumbnailUrl;
  
  @NotNull(message = "Teaching type is required")
  private TeachingType type;
  
  @NotBlank(message = "Tags are required (comma-separated)")
  private String tags; 
  
  private Long seriesId;
  
  private Integer seriesPart;
  
  @NotNull(message = "Record datetime is required")
  private LocalDateTime taughtAt;
}
