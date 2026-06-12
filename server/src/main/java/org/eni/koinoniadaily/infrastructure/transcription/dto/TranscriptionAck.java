package org.eni.koinoniadaily.infrastructure.transcription.dto;

public record TranscriptionAck(
    boolean success,
    String message,
    Long transcriptId
) {}
