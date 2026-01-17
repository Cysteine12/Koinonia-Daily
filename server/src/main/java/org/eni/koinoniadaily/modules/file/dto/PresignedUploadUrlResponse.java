package org.eni.koinoniadaily.modules.file.dto;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PresignedUploadUrlResponse {
  
  private String presignedUrl;

  private String publicUrl;

  private String key;

  private Map<String, List<String>> requiredHeaders;
}
