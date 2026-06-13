package org.eni.koinoniadaily.modules.transcript;

import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.exceptions.UnauthorizedException;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptCallbackPayload;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptPageResponse;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptRequest;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptResponse;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptTriggerRequest;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/transcripts")
@RequiredArgsConstructor
@Validated
public class TranscriptController {
  
  private final TranscriptService transcriptService;
  private final TranscriptWorkflowService transcriptWorkflowService;
  private final AppProperties props;

  @GetMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<PageResponse<TranscriptPageResponse>>> getTranscripts(
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,
      @RequestParam(defaultValue = "50") @Positive @Max(100) int size
  ) {
    PageResponse<TranscriptPageResponse> response = transcriptService.getTranscripts(page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @GetMapping("/search")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<PageResponse<TranscriptPageResponse>>> searchTranscripts(
      @RequestParam @NotBlank String q,
      @RequestParam(defaultValue = "0") @PositiveOrZero int page,
      @RequestParam(defaultValue = "50") @Positive @Max(100) int size
  ) {
    PageResponse<TranscriptPageResponse> response = transcriptService.searchTranscripts(q, page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<TranscriptResponse>> getTranscript(
      @PathVariable @Positive Long id
  ) {
    TranscriptResponse response = transcriptService.getTranscriptById(id);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<TranscriptResponse>> createTranscript(
      @RequestBody @Valid TranscriptRequest request
  ) {
    TranscriptResponse response = transcriptService.createTranscript(request);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(SuccessResponse.of(response, "Transcript created successfully"));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<TranscriptResponse>> updateTranscript(
      @PathVariable @Positive Long id,
      @RequestBody @Valid TranscriptRequest request
  ) {
    TranscriptResponse response = transcriptService.updateTranscript(id, request);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> deleteTranscript(
      @PathVariable @Positive Long id
  ) {
    transcriptService.deleteTranscript(id);

    return ResponseEntity.ok(SuccessResponse.message("Transcript deleted successfully"));
  }

  @PostMapping("/workflow/trigger")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> triggerTranscription(
      @RequestBody @Valid TranscriptTriggerRequest request
  ) {
    transcriptWorkflowService.trigger(request);

    return ResponseEntity.ok(SuccessResponse.message("Transcription job dispatched successfully"));
  }

  @PostMapping("/workflow/callback")
  public ResponseEntity<SuccessResponse<Void>> handleCallback(
      @RequestHeader("x-callback-secret") String callbackSecret,
      @RequestBody @Valid TranscriptCallbackPayload payload
  ) {
    String expectedSecret = props.getTranscription().getModal().getCallbackSecret();

    if (!Objects.equals(expectedSecret, callbackSecret)) {
      throw new UnauthorizedException("CALLBACK_SECRET_INVALID", "Invalid transcription callback secret");
    }

    transcriptWorkflowService.handleCallback(payload);

    return ResponseEntity.ok(SuccessResponse.message("Callback processed"));
  }
}
