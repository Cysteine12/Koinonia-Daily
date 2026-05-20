package org.eni.koinoniadaily.infrastructure.embedding.dispatchers;

import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;

public interface EmbeddingJobDispatcher {

  void dispatch(EmbeddingJob job);
}
