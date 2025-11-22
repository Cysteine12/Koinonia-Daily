package org.eni.koinonia_daily.modules.teaching;

import java.time.LocalDateTime;

import org.eni.koinonia_daily.modules.series.Series;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeachingDto {

  @NotBlank(message = "Title is required")
  private String title;
  
  @NotEmpty(message = "Atleast one scriptural reference is required")
  private String[] scripturalReferences;
  
  @NotBlank(message = "Message is required")
  private String message;
  
  @NotBlank(message = "Summary is required")
  private String summary;
  
  @NotBlank(message = "Audio URL is required")
  private String audioUrl;
  
  @NotBlank(message = "Video URL is required")
  private String videoUrl;
  
  @NotBlank(message = "Thumbnail is required")
  private String thumbnailUrl;
  
  @NotNull(message = "Teaching type is required")
  private TeachingType type;
  
  @NotEmpty(message = "Tags are required")
  private String[] tags; 
  
  private Series series;
  
  private int seriesPart;
  
  @NotNull(message = "Record datetime is required")
  private LocalDateTime taughtAt;
}
