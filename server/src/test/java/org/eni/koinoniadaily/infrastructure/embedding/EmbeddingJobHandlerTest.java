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
  private EmbeddingJobHandler embeddingJobHandler;

  @Test
  void handle_validJob_delegatesToPipelineService() {
    EmbeddingJob job = new EmbeddingJob(1L);

    embeddingJobHandler.handle(job);

    verify(pipelineService, times(1)).process(job);
  }

  @Test
  void handle_passesJobUnchangedToPipelineService() {
    EmbeddingJob job = new EmbeddingJob(999L);

    embeddingJobHandler.handle(job);

    verify(pipelineService).process(job);
    verifyNoMoreInteractions(pipelineService);
  }

  @Test
  void handle_pipelineServiceThrows_exceptionPropagates() {
    EmbeddingJob job = new EmbeddingJob(1L);
    doThrow(new RuntimeException("Pipeline failure")).when(pipelineService).process(job);

    org.assertj.core.api.Assertions.assertThatThrownBy(() -> embeddingJobHandler.handle(job))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("Pipeline failure");
  }

  @Test
  void handle_differentTeachingIds_eachDelegatedToService() {
    EmbeddingJob job1 = new EmbeddingJob(10L);
    EmbeddingJob job2 = new EmbeddingJob(20L);

    embeddingJobHandler.handle(job1);
    embeddingJobHandler.handle(job2);

    verify(pipelineService).process(job1);
    verify(pipelineService).process(job2);
  }
}