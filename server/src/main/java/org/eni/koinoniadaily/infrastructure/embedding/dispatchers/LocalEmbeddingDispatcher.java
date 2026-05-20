package org.eni.koinoniadaily.infrastructure.embedding.dispatchers;

import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.infrastructure.embedding.EmbeddingJobHandler;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.embedding.queue.provider", havingValue = "local", matchIfMissing = true)
@Qualifier("local")
public class LocalEmbeddingDispatcher implements EmbeddingJobDispatcher {

  private final EmbeddingJobHandler handler;

  @Async("embeddingExecutor")
  @Override
  public void dispatch(EmbeddingJob job) {

    handler.handle(job);
  }
}
