package org.eni.koinoniadaily.infrastructure.embedding.dispatchers;

import org.eni.koinoniadaily.infrastructure.embedding.EmbeddingJobHandler;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalEmbeddingDispatcherTest {

  @Mock
  private EmbeddingJobHandler handler;

  @InjectMocks
  private LocalEmbeddingDispatcher dispatcher;

  @Test
  void dispatch_successfulHandling_delegatesToHandler() {
    EmbeddingJob job = new EmbeddingJob(1L);

    dispatcher.dispatch(job);

    verify(handler).handle(job);
  }

  @Test
  void dispatch_handlerThrowsRuntimeException_doesNotPropagate() {
    EmbeddingJob job = new EmbeddingJob(2L);
    doThrow(new RuntimeException("Pipeline failed")).when(handler).handle(job);

    // dispatch() catches Throwable internally - must not propagate
    assertThatCode(() -> dispatcher.dispatch(job)).doesNotThrowAnyException();
  }

  @Test
  void dispatch_handlerThrowsError_doesNotPropagate() {
    EmbeddingJob job = new EmbeddingJob(3L);
    doThrow(new OutOfMemoryError("OOM")).when(handler).handle(job);

    // dispatch() catches Throwable (not just Exception) - Errors are also caught
    assertThatCode(() -> dispatcher.dispatch(job)).doesNotThrowAnyException();
  }

  @Test
  void dispatch_delegatesCorrectJobToHandler() {
    EmbeddingJob job = new EmbeddingJob(42L);

    dispatcher.dispatch(job);

    verify(handler, times(1)).handle(job);
    verifyNoMoreInteractions(handler);
  }

  @Test
  void dispatch_multipleJobs_eachDelegatedToHandler() {
    EmbeddingJob job1 = new EmbeddingJob(1L);
    EmbeddingJob job2 = new EmbeddingJob(2L);
    EmbeddingJob job3 = new EmbeddingJob(3L);

    dispatcher.dispatch(job1);
    dispatcher.dispatch(job2);
    dispatcher.dispatch(job3);

    verify(handler).handle(job1);
    verify(handler).handle(job2);
    verify(handler).handle(job3);
  }
}