package org.eni.koinoniadaily.modules.chunkembedding;

import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.infrastructure.embedding.dispatchers.EmbeddingJobDispatcher;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.eni.koinoniadaily.modules.chunkembedding.dto.ChunkEmbeddingRequest;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.TeachingStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChunkEmbeddingServiceTest {

  @Mock
  private EmbeddingJobDispatcher embeddingJobDispatcher;

  @Mock
  private TeachingRepository teachingRepository;

  @InjectMocks
  private ChunkEmbeddingService service;

  private Teaching makeTeaching(Long id, TeachingStatus status) {
    Teaching teaching = new Teaching();
    teaching.setId(id);
    teaching.setStatus(status);
    return teaching;
  }

  private ChunkEmbeddingRequest makeRequest(Long... ids) {
    ChunkEmbeddingRequest request = new ChunkEmbeddingRequest();
    request.setTeachingIds(List.of(ids));
    return request;
  }

  @Nested
  class TriggerEmbeddingTests {

    @Test
    void triggerEmbedding_allTeachingsChunked_dispatchesJobsSuccessfully() {
      Teaching t1 = makeTeaching(1L, TeachingStatus.CHUNKED);
      Teaching t2 = makeTeaching(2L, TeachingStatus.EMBEDDED);
      ChunkEmbeddingRequest request = makeRequest(1L, 2L);

      when(teachingRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(t1, t2));

      service.triggerEmbedding(request);

      ArgumentCaptor<EmbeddingJob> captor = ArgumentCaptor.forClass(EmbeddingJob.class);
      verify(embeddingJobDispatcher, times(2)).dispatch(captor.capture());

      List<EmbeddingJob> dispatchedJobs = captor.getAllValues();
      assertThat(dispatchedJobs).extracting(EmbeddingJob::teachingId)
          .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void triggerEmbedding_invalidId_countMismatch_throwsValidationException() {
      // Only 1 teaching found, but 2 requested
      Teaching t1 = makeTeaching(1L, TeachingStatus.CHUNKED);
      ChunkEmbeddingRequest request = makeRequest(1L, 999L);

      when(teachingRepository.findAllById(List.of(1L, 999L))).thenReturn(List.of(t1));

      assertThatThrownBy(() -> service.triggerEmbedding(request))
          .isInstanceOf(ValidationException.class)
          .hasMessageContaining("Invalid teaching id supplied");

      verifyNoInteractions(embeddingJobDispatcher);
    }

    @Test
    void triggerEmbedding_pendingTeaching_throwsValidationException() {
      Teaching pending = makeTeaching(1L, TeachingStatus.PENDING);
      ChunkEmbeddingRequest request = makeRequest(1L);

      when(teachingRepository.findAllById(List.of(1L))).thenReturn(List.of(pending));

      assertThatThrownBy(() -> service.triggerEmbedding(request))
          .isInstanceOf(ValidationException.class)
          .hasMessageContaining("has not been chunked");

      verifyNoInteractions(embeddingJobDispatcher);
    }

    @Test
    void triggerEmbedding_pendingTeaching_exceptionMessageContainsTeachingId() {
      Teaching pending = makeTeaching(42L, TeachingStatus.PENDING);
      ChunkEmbeddingRequest request = makeRequest(42L);

      when(teachingRepository.findAllById(List.of(42L))).thenReturn(List.of(pending));

      assertThatThrownBy(() -> service.triggerEmbedding(request))
          .isInstanceOf(ValidationException.class)
          .hasMessageContaining("42");
    }

    @Test
    void triggerEmbedding_queueSaturated_throwsValidationExceptionWithCode() {
      Teaching chunked = makeTeaching(1L, TeachingStatus.CHUNKED);
      ChunkEmbeddingRequest request = makeRequest(1L);

      when(teachingRepository.findAllById(List.of(1L))).thenReturn(List.of(chunked));
      doThrow(new RejectedExecutionException("Queue full")).when(embeddingJobDispatcher).dispatch(any());

      assertThatThrownBy(() -> service.triggerEmbedding(request))
          .isInstanceOf(ValidationException.class)
          .hasMessageContaining("Queue saturated");

      ValidationException ex = null;
      try {
        service.triggerEmbedding(request);
      } catch (ValidationException e) {
        ex = e;
      }
      assertThat(ex).isNotNull();
      assertThat(ex.getCode()).isEqualTo("EMBEDDING_REJECTED");
    }

    @Test
    void triggerEmbedding_embeddedStatus_dispatchesSuccessfully() {
      Teaching embedded = makeTeaching(1L, TeachingStatus.EMBEDDED);
      ChunkEmbeddingRequest request = makeRequest(1L);

      when(teachingRepository.findAllById(List.of(1L))).thenReturn(List.of(embedded));

      service.triggerEmbedding(request);

      verify(embeddingJobDispatcher).dispatch(new EmbeddingJob(1L));
    }

    @Test
    void triggerEmbedding_failedStatus_dispatchesSuccessfully() {
      Teaching failed = makeTeaching(1L, TeachingStatus.FAILED);
      ChunkEmbeddingRequest request = makeRequest(1L);

      when(teachingRepository.findAllById(List.of(1L))).thenReturn(List.of(failed));

      service.triggerEmbedding(request);

      verify(embeddingJobDispatcher).dispatch(new EmbeddingJob(1L));
    }

    @Test
    void triggerEmbedding_singleTeaching_dispatchesExactlyOneJob() {
      Teaching chunked = makeTeaching(5L, TeachingStatus.CHUNKED);
      ChunkEmbeddingRequest request = makeRequest(5L);

      when(teachingRepository.findAllById(List.of(5L))).thenReturn(List.of(chunked));

      service.triggerEmbedding(request);

      verify(embeddingJobDispatcher, times(1)).dispatch(new EmbeddingJob(5L));
    }

    @Test
    void triggerEmbedding_multiplePendingTeachings_throwsOnFirstPending() {
      Teaching pending1 = makeTeaching(1L, TeachingStatus.PENDING);
      Teaching pending2 = makeTeaching(2L, TeachingStatus.PENDING);
      ChunkEmbeddingRequest request = makeRequest(1L, 2L);

      when(teachingRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(pending1, pending2));

      assertThatThrownBy(() -> service.triggerEmbedding(request))
          .isInstanceOf(ValidationException.class);

      verifyNoInteractions(embeddingJobDispatcher);
    }

    @Test
    void triggerEmbedding_embeddingStatus_dispatchesSuccessfully() {
      // EMBEDDING status is not PENDING, so it should be dispatched
      Teaching embedding = makeTeaching(1L, TeachingStatus.EMBEDDING);
      ChunkEmbeddingRequest request = makeRequest(1L);

      when(teachingRepository.findAllById(List.of(1L))).thenReturn(List.of(embedding));

      service.triggerEmbedding(request);

      verify(embeddingJobDispatcher).dispatch(any(EmbeddingJob.class));
    }
  }
}