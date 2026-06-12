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

    Map<String, Object> metadata
) {}
