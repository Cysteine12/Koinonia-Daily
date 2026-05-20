package org.eni.koinoniadaily.infrastructure.embedding;

import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.eni.koinoniadaily.modules.chunkembedding.EmbeddingPipelineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingJobHandlerTest {

  @Mock
  private EmbeddingPipelineService pipelineService;

  @InjectMocks
  private EmbeddingJobHandler handler;

  @Test
  void handle_delegatesToPipelineService() {
    EmbeddingJob job = new EmbeddingJob(1L);

    handler.handle(job);

    verify(pipelineService).process(job);
  }

  @Test
  void handle_passesJobUnchangedToPipeline() {
    EmbeddingJob job = new EmbeddingJob(99L);

    handler.handle(job);

    verify(pipelineService, times(1)).process(job);
    verifyNoMoreInteractions(pipelineService);
  }

  @Test
  void handle_multipleJobs_eachDelegatedInOrder() {
    EmbeddingJob job1 = new EmbeddingJob(10L);
    EmbeddingJob job2 = new EmbeddingJob(20L);

    handler.handle(job1);
    handler.handle(job2);

    var inOrder = inOrder(pipelineService);
    inOrder.verify(pipelineService).process(job1);
    inOrder.verify(pipelineService).process(job2);
  }
}