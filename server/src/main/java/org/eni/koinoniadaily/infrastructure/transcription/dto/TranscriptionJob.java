package org.eni.koinoniadaily.infrastructure.transcription.dto;

public record TranscriptionJob(
    Long transcriptId,
    String audioUrl,
    String callbackUrl
) {}
