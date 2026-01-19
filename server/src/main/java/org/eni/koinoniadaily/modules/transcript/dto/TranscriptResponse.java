package org.eni.koinoniadaily.modules.transcript.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TranscriptResponse {

  private Long id;
  
  private String title;
  
  private String message;
  
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}