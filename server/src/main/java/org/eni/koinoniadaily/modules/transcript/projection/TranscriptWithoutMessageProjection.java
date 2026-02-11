package org.eni.koinoniadaily.modules.transcript.projection;

import java.time.Instant;

public interface TranscriptWithoutMessageProjection {

  Long getId();

  String getTitle();
  
  Instant getCreatedAt();
  
  Instant getUpdatedAt();  
}
