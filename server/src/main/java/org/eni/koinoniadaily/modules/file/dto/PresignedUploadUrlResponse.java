package org.eni.koinoniadaily.modules.file.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PresignedUploadUrlResponse {
  
  private String presignedUrl;

  private String publicUrl;

  private String key;
}
