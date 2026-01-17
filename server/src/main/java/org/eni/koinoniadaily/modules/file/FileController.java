package org.eni.koinoniadaily.modules.file;

import org.eni.koinoniadaily.modules.file.dto.PresignedUploadUrlRequest;
import org.eni.koinoniadaily.modules.file.dto.PresignedUploadUrlResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/file")
@Validated
public class FileController {
  
  private final FileService fileService;

  @GetMapping("/presign")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<PresignedUploadUrlResponse>> generateUploadPresignedUrl(
    @RequestBody @Valid PresignedUploadUrlRequest request
  ) {
    PresignedUploadUrlResponse response = fileService.generatePresignedUploadUrl(request);

    return ResponseEntity.ok(SuccessResponse.of(response, "Presigned upload URL generated successfully"));
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> deleteFile(
    @RequestParam @NotBlank String filename
  ) {
    fileService.deleteObject(filename);

    return ResponseEntity.ok(SuccessResponse.message("File deleted successfully"));
  }
}
