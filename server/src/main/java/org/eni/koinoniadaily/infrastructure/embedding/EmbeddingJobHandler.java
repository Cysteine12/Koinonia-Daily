package org.eni.koinoniadaily.infrastructure.embedding;

import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.eni.koinoniadaily.modules.chunkembedding.EmbeddingPipelineService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmbeddingJobHandler {

  private final EmbeddingPipelineService pipelineService;

  public void handle(EmbeddingJob job) {

    pipelineService.process(job);
  }
}
