package org.eni.koinoniadaily.modules.transcript;

import org.eni.koinoniadaily.modules.transcript.dto.TranscriptPageResponse;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptRequest;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transcripts")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('ADMIN')")
public class TranscriptController {
  
  private final TranscriptService transcriptService;

  @GetMapping
  public ResponseEntity<SuccessResponse<PageResponse<TranscriptPageResponse>>> getTranscripts(
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,
      @RequestParam(defaultValue = "50") @Positive @Max(100) int size
  ) {
    PageResponse<TranscriptPageResponse> response = transcriptService.getTranscripts(page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @GetMapping("/search")
  public ResponseEntity<SuccessResponse<PageResponse<TranscriptPageResponse>>> searchTranscripts(
      @RequestParam @NotBlank String q,
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,
      @RequestParam(defaultValue = "50") @Positive @Max(100) int size
  ) {
    PageResponse<TranscriptPageResponse> response = transcriptService.searchTranscripts(q, page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SuccessResponse<TranscriptResponse>> getTranscript(
      @PathVariable @Positive Long id
  ) {
    TranscriptResponse response = transcriptService.getTranscriptById(id);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PostMapping
  public ResponseEntity<SuccessResponse<TranscriptResponse>> createTranscript(
      @RequestBody @Valid TranscriptRequest request
  ) {
    TranscriptResponse response = transcriptService.createTranscript(request);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(SuccessResponse.of(response, "Transcript created successfully"));
  }

  @PutMapping("/{id}")
  public ResponseEntity<SuccessResponse<TranscriptResponse>> updateTranscript(
      @PathVariable @Positive Long id,
      @RequestBody @Valid TranscriptRequest request
  ) {
    TranscriptResponse response = transcriptService.updateTranscript(id, request);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<SuccessResponse<Void>> deleteTranscript(
      @PathVariable @Positive Long id
  ) {
    transcriptService.deleteTranscript(id);

    return ResponseEntity.ok(SuccessResponse.message("Transcript deleted successfully"));
  }
}
