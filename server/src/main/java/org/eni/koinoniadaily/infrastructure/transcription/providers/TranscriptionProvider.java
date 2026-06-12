package org.eni.koinoniadaily.infrastructure.transcription.providers;

import org.eni.koinoniadaily.infrastructure.transcription.dto.TranscriptionAck;
import org.eni.koinoniadaily.infrastructure.transcription.dto.TranscriptionJob;

public interface TranscriptionProvider {

  TranscriptionAck dispatch(TranscriptionJob job);
}
