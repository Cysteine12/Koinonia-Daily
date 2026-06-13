package org.eni.koinoniadaily.modules.transcript.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Map;

public record TranscriptCallbackPayload (

    @NotNull
    @Positive
    Long transcriptId,

    @NotNull
    Boolean success,

    String text,

    String error,

    @NotNull
    Map<String, Object> metadata
) {
    public TranscriptCallbackPayload {
        metadata = Map.copyOf(metadata);
    }
}
