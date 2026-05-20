package org.eni.koinoniadaily.infrastructure.embedding.dispatchers;

import org.eni.koinoniadaily.infrastructure.embedding.EmbeddingJobHandler;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalEmbeddingDispatcherTest {

  @Mock
  private EmbeddingJobHandler handler;

  @InjectMocks
  private LocalEmbeddingDispatcher localEmbeddingDispatcher;

  // -----------------------------------------------------------------------
  // dispatch() - successful invocation
  // -----------------------------------------------------------------------

  @Test
  void dispatch_validJob_delegatesToHandler() {
    EmbeddingJob job = new EmbeddingJob(1L);

    localEmbeddingDispatcher.dispatch(job);

    verify(handler, times(1)).handle(job);
  }

  @Test
  void dispatch_passesJobUnchangedToHandler() {
    EmbeddingJob job = new EmbeddingJob(42L);

    localEmbeddingDispatcher.dispatch(job);

    verify(handler).handle(job);
    verifyNoMoreInteractions(handler);
  }

  // -----------------------------------------------------------------------
  // dispatch() - exception handling
  // -----------------------------------------------------------------------

  @Test
  void dispatch_handlerThrowsRuntimeException_doesNotPropagate() {
    EmbeddingJob job = new EmbeddingJob(1L);
    doThrow(new RuntimeException("Processing error")).when(handler).handle(job);

    // Should NOT throw - the dispatcher catches Throwable internally
    localEmbeddingDispatcher.dispatch(job);

    verify(handler).handle(job);
  }

  @Test
  void dispatch_handlerThrowsError_doesNotPropagate() {
    EmbeddingJob job = new EmbeddingJob(1L);
    doThrow(new OutOfMemoryError("OOM")).when(handler).handle(job);

    // Should NOT throw - catches Throwable (which includes Error)
    localEmbeddingDispatcher.dispatch(job);

    verify(handler).handle(job);
  }

  @Test
  void dispatch_handlerThrowsCheckedException_doesNotPropagate() {
    EmbeddingJob job = new EmbeddingJob(1L);
    doThrow(new RuntimeException(new Exception("Checked exception wrapped"))).when(handler).handle(job);

    // Should NOT throw
    localEmbeddingDispatcher.dispatch(job);

    verify(handler).handle(job);
  }

  // -----------------------------------------------------------------------
  // dispatch() - multiple calls
  // -----------------------------------------------------------------------

  @Test
  void dispatch_calledMultipleTimes_handleCalledEachTime() {
    EmbeddingJob job1 = new EmbeddingJob(1L);
    EmbeddingJob job2 = new EmbeddingJob(2L);
    EmbeddingJob job3 = new EmbeddingJob(3L);

    localEmbeddingDispatcher.dispatch(job1);
    localEmbeddingDispatcher.dispatch(job2);
    localEmbeddingDispatcher.dispatch(job3);

    verify(handler).handle(job1);
    verify(handler).handle(job2);
    verify(handler).handle(job3);
  }

  @Test
  void dispatch_firstJobFails_secondJobStillDispatched() {
    EmbeddingJob job1 = new EmbeddingJob(1L);
    EmbeddingJob job2 = new EmbeddingJob(2L);

    doThrow(new RuntimeException("First job fails")).when(handler).handle(job1);

    localEmbeddingDispatcher.dispatch(job1);
    localEmbeddingDispatcher.dispatch(job2);

    verify(handler).handle(job1);
    verify(handler).handle(job2);
  }
}