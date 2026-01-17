package org.eni.koinoniadaily.modules.file.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresignedUploadUrlRequest {
  
  @NotBlank(message = "File extension is required")
  @Pattern(
      regexp = "^(jpg|jpeg|png|gif|webp)$", 
      message = "Invalid file extension. Allowed extensions are: jpg, jpeg, png, gif, webp"
  )
  private String fileExtension;
  
  @NotBlank(message = "Content type is required")
  @Pattern(
      regexp = "^(image/jpeg|image/png|image/gif|image/webp)$", 
      message = "Invalid content type. Allowed types are: image/jpeg, image/png, image/gif, image/webp"
  )
  private String contentType;
}
