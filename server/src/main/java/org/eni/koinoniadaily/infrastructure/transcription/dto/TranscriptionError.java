package org.eni.koinoniadaily.infrastructure.transcription.dto;

import java.util.Map;

public record TranscriptionError(
    boolean success,
    Long transcriptId,
    String error,
    Map<String, String> metadata
) {}
