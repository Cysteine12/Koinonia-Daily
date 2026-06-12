package org.eni.koinoniadaily.modules.transcript.dto;

import org.eni.koinoniadaily.modules.transcript.TranscriptStatus;

import java.time.Instant;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TranscriptResponse {

  private Long id;
  
  private String title;
  
  private String message;

  private TranscriptStatus status;

  private Map<String, Object> metadata;
  
  private Instant createdAt;

  private Instant updatedAt;
}