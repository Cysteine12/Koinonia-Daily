package org.eni.koinoniadaily.infrastructure.transcription.dto;

import java.util.Map;

public record Transcription(
    boolean success,
    Long transcriptId,
    String filename,
    String text,
    int duration,
    Map<String, String> metadata
) {
}
