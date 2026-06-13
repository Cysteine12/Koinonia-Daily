package org.eni.koinoniadaily.infrastructure.transcription.providers;

import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.config.AppProperties;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.infrastructure.transcription.dto.TranscriptionAck;
import org.eni.koinoniadaily.infrastructure.transcription.dto.TranscriptionJob;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.transcription.provider", havingValue = "modal")
@Qualifier("modal")
class ModalTranscriptionProvider implements TranscriptionProvider {

  private final RestClient restClient;

  public ModalTranscriptionProvider(RestClient.Builder builder, AppProperties props) {

    this.restClient = builder
        .baseUrl(props.getTranscription().getModal().getUrl())
        .defaultHeader("X-Api-Key", props.getTranscription().getModal().getApiKey())
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  @Override
  @Retryable(
      includes = {
          HttpServerErrorException.class,
          ResourceAccessException.class
      },
      excludes = {
          ValidationException.class
      },
      maxRetries = 2,
      delay = 1000,
      multiplier = 2,
      maxDelay = 5000,
      jitter = 500
  )
  public TranscriptionAck dispatch(TranscriptionJob job) {

    log.info("Dispatching transcription job to Modal — transcriptId={}", job.transcriptId());

    try {
      TranscriptionAck ack = restClient.post()
          .uri("/transcribe")
          .contentType(MediaType.APPLICATION_JSON)
          .body(job)
          .retrieve()
          .body(TranscriptionAck.class);

      log.info("Modal accepted transcription job — transcriptId={}", job.transcriptId());

      return ack;

    } catch (HttpClientErrorException ex) {

      if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        log.error("Modal rejected request — invalid API key. transcriptId={}", job.transcriptId());
        throw new ValidationException("TRANSCRIPTION_AUTH_FAILED", "Transcription provider rejected the request");
      }

      log.error("Modal returned client error {} for transcriptId={}", ex.getStatusCode(), job.transcriptId());
      throw new ValidationException("TRANSCRIPTION_DISPATCH_FAILED", "Transcription dispatch failed: " + ex.getMessage());

    }
  }
}
