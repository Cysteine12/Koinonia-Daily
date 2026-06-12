package org.eni.koinoniadaily.modules.transcript;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.infrastructure.transcription.dto.TranscriptionJob;
import org.eni.koinoniadaily.infrastructure.transcription.providers.TranscriptionProvider;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptCallbackPayload;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptTriggerRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranscriptWorkflowService {

  private final TranscriptRepository transcriptRepository;
  private final TranscriptionProvider transcriptionProvider;
  private final AppProperties appProperties;

  private static final String CALLBACK_PATH = "/api/v1/transcripts/workflow/callback";

  public void trigger(TranscriptTriggerRequest request) {

    log.info("Transcription trigger requested for transcriptId={}", request.getTranscriptId());

    Transcript transcript = transcriptRepository.findById(request.getTranscriptId())
        .orElseThrow(() -> new NotFoundException("Transcript not found"));

    boolean isStuck = transcript.getStatus() == TranscriptStatus.IN_PROGRESS
        && transcript.getUpdatedAt().isBefore(Instant.now().minus(Duration.ofHours(1)));

    if (transcript.getStatus() == TranscriptStatus.IN_PROGRESS && !isStuck) {
      throw new ValidationException("TRANSCRIPTION_IN_PROGRESS", "Transcription is already in progress for this transcript");
    }

    TranscriptionJob job = new TranscriptionJob(
        transcript.getId(),
        request.getAudioUrl(),
        appProperties.getBaseUrl() + CALLBACK_PATH
    );

    transcript.setStatus(TranscriptStatus.IN_PROGRESS);
    transcriptRepository.save(transcript);

    try {
      transcriptionProvider.dispatch(job);

      log.info("Transcription dispatched successfully for transcriptId={}", transcript.getId());
    } catch (Exception ex) {

      transcript.setStatus(TranscriptStatus.FAILED);
      transcriptRepository.save(transcript);

      log.error("Transcription dispatch failed for transcriptId={}", transcript.getId(), ex);
    }

  }

  @Transactional
  public void handleCallback(TranscriptCallbackPayload payload) {

    log.info("Transcription callback received for transcriptId={} success={}", payload.transcriptId(), payload.success());

    Transcript transcript = transcriptRepository.findById(payload.transcriptId())
        .orElseThrow(() -> new NotFoundException("Transcript not found"));

    if (!payload.success()) {
      Map<String, Object> metadata = new HashMap<>(payload.metadata());
      metadata.put("error", payload.error());

      transcript.setStatus(TranscriptStatus.FAILED);
      transcript.setMetadata(metadata);
    }

    transcript.setMessage(payload.text());
    transcript.setStatus(TranscriptStatus.COMPLETED);
    transcript.setMetadata(payload.metadata());

    transcriptRepository.save(transcript);

    log.info("Transcript updated after callback for transcriptId={} status={}",
        transcript.getId(), transcript.getStatus());
  }
}
