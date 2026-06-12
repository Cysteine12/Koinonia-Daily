package org.eni.koinoniadaily.modules.transcript.projection;

import org.eni.koinoniadaily.modules.transcript.TranscriptStatus;

import java.time.Instant;

public interface TranscriptWithoutMessageProjection {

  Long getId();

  String getTitle();

  TranscriptStatus getStatus();
  
  Instant getCreatedAt();
  
  Instant getUpdatedAt();  
}
