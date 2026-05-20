package org.eni.koinoniadaily.modules.chunkembedding;

import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.infrastructure.embedding.dispatchers.EmbeddingJobDispatcher;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.eni.koinoniadaily.modules.chunkembedding.dto.ChunkEmbeddingRequest;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.TeachingStatus;
import org.junit.jupiter.api.BeforeEach;
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
  private ChunkEmbeddingService chunkEmbeddingService;

  private ChunkEmbeddingRequest request;

  @BeforeEach
  void setUp() {
    request = new ChunkEmbeddingRequest();
  }

  // -----------------------------------------------------------------------
  // triggerEmbedding - invalid teaching IDs
  // -----------------------------------------------------------------------

  @Test
  void triggerEmbedding_allIdsNotFound_throwsValidationException() {
    request.setTeachingIds(List.of(1L, 2L, 3L));

    // Repository returns fewer results than requested
    when(teachingRepository.findAllById(List.of(1L, 2L, 3L))).thenReturn(List.of());

    assertThatThrownBy(() -> chunkEmbeddingService.triggerEmbedding(request))
        .isInstanceOf(ValidationException.class)
        .hasMessageContaining("Invalid teaching id supplied");
  }

  @Test
  void triggerEmbedding_someIdsNotFound_throwsValidationException() {
    request.setTeachingIds(List.of(1L, 2L, 3L));

    Teaching teaching1 = buildTeaching(1L, TeachingStatus.CHUNKED);
    // Only returns 2 of 3 requested teachings
    when(teachingRepository.findAllById(List.of(1L, 2L, 3L))).thenReturn(List.of(teaching1));

    assertThatThrownBy(() -> chunkEmbeddingService.triggerEmbedding(request))
        .isInstanceOf(ValidationException.class)
        .hasMessageContaining("Invalid teaching id supplied");

    verifyNoInteractions(embeddingJobDispatcher);
  }

  // -----------------------------------------------------------------------
  // triggerEmbedding - PENDING status guard
  // -----------------------------------------------------------------------

  @Test
  void triggerEmbedding_pendingTeaching_throwsValidationException() {
    request.setTeachingIds(List.of(1L));

    Teaching pendingTeaching = buildTeaching(1L, TeachingStatus.PENDING);
    when(teachingRepository.findAllById(List.of(1L))).thenReturn(List.of(pendingTeaching));

    assertThatThrownBy(() -> chunkEmbeddingService.triggerEmbedding(request))
        .isInstanceOf(ValidationException.class)
        .hasMessageContaining("has not been chunked");

    verifyNoInteractions(embeddingJobDispatcher);
  }

  @Test
  void triggerEmbedding_mixedStatusWithOnePending_throwsValidationException() {
    request.setTeachingIds(List.of(1L, 2L));

    Teaching chunkedTeaching = buildTeaching(1L, TeachingStatus.CHUNKED);
    Teaching pendingTeaching = buildTeaching(2L, TeachingStatus.PENDING);
    when(teachingRepository.findAllById(List.of(1L, 2L)))
        .thenReturn(List.of(chunkedTeaching, pendingTeaching));

    assertThatThrownBy(() -> chunkEmbeddingService.triggerEmbedding(request))
        .isInstanceOf(ValidationException.class)
        .hasMessageContaining("has not been chunked");

    verifyNoInteractions(embeddingJobDispatcher);
  }

  @Test
  void triggerEmbedding_pendingTeachingMessage_includesTeachingId() {
    request.setTeachingIds(List.of(42L));

    Teaching pendingTeaching = buildTeaching(42L, TeachingStatus.PENDING);
    when(teachingRepository.findAllById(List.of(42L))).thenReturn(List.of(pendingTeaching));

    assertThatThrownBy(() -> chunkEmbeddingService.triggerEmbedding(request))
        .isInstanceOf(ValidationException.class)
        .hasMessageContaining("42");
  }

  // -----------------------------------------------------------------------
  // triggerEmbedding - successful dispatch
  // -----------------------------------------------------------------------

  @Test
  void triggerEmbedding_singleChunkedTeaching_dispatchesSingleJob() {
    request.setTeachingIds(List.of(1L));

    Teaching chunkedTeaching = buildTeaching(1L, TeachingStatus.CHUNKED);
    when(teachingRepository.findAllById(List.of(1L))).thenReturn(List.of(chunkedTeaching));

    chunkEmbeddingService.triggerEmbedding(request);

    ArgumentCaptor<EmbeddingJob> captor = ArgumentCaptor.forClass(EmbeddingJob.class);
    verify(embeddingJobDispatcher, times(1)).dispatch(captor.capture());
    assertThat(captor.getValue().teachingId()).isEqualTo(1L);
  }

  @Test
  void triggerEmbedding_multipleChunkedTeachings_dispatchesOneJobEach() {
    request.setTeachingIds(List.of(1L, 2L, 3L));

    Teaching t1 = buildTeaching(1L, TeachingStatus.CHUNKED);
    Teaching t2 = buildTeaching(2L, TeachingStatus.EMBEDDED);
    Teaching t3 = buildTeaching(3L, TeachingStatus.FAILED);
    when(teachingRepository.findAllById(List.of(1L, 2L, 3L))).thenReturn(List.of(t1, t2, t3));

    chunkEmbeddingService.triggerEmbedding(request);

    verify(embeddingJobDispatcher, times(3)).dispatch(any(EmbeddingJob.class));
  }

  @Test
  void triggerEmbedding_chunkedTeaching_dispatchedJobHasCorrectTeachingId() {
    request.setTeachingIds(List.of(99L));

    Teaching teaching = buildTeaching(99L, TeachingStatus.CHUNKED);
    when(teachingRepository.findAllById(List.of(99L))).thenReturn(List.of(teaching));

    chunkEmbeddingService.triggerEmbedding(request);

    ArgumentCaptor<EmbeddingJob> captor = ArgumentCaptor.forClass(EmbeddingJob.class);
    verify(embeddingJobDispatcher).dispatch(captor.capture());
    assertThat(captor.getValue().teachingId()).isEqualTo(99L);
  }

  @Test
  void triggerEmbedding_embeddingStatus_nonPendingStatuses_areAllAccepted() {
    // EMBEDDED and FAILED teachings should still be re-dispatchable
    request.setTeachingIds(List.of(1L, 2L));

    Teaching embedded = buildTeaching(1L, TeachingStatus.EMBEDDED);
    Teaching failed = buildTeaching(2L, TeachingStatus.FAILED);
    when(teachingRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(embedded, failed));

    chunkEmbeddingService.triggerEmbedding(request);

    verify(embeddingJobDispatcher, times(2)).dispatch(any(EmbeddingJob.class));
  }

  // -----------------------------------------------------------------------
  // triggerEmbedding - queue saturation (RejectedExecutionException)
  // -----------------------------------------------------------------------

  @Test
  void triggerEmbedding_dispatcherRejectsExecution_throwsValidationException() {
    request.setTeachingIds(List.of(1L));

    Teaching teaching = buildTeaching(1L, TeachingStatus.CHUNKED);
    when(teachingRepository.findAllById(List.of(1L))).thenReturn(List.of(teaching));
    doThrow(new RejectedExecutionException("Queue full"))
        .when(embeddingJobDispatcher).dispatch(any(EmbeddingJob.class));

    assertThatThrownBy(() -> chunkEmbeddingService.triggerEmbedding(request))
        .isInstanceOf(ValidationException.class)
        .hasMessageContaining("Queue saturated");
  }

  @Test
  void triggerEmbedding_dispatcherRejectsExecution_validationExceptionCodeIsEmbeddingRejected() {
    request.setTeachingIds(List.of(1L));

    Teaching teaching = buildTeaching(1L, TeachingStatus.CHUNKED);
    when(teachingRepository.findAllById(List.of(1L))).thenReturn(List.of(teaching));
    doThrow(new RejectedExecutionException("Queue full"))
        .when(embeddingJobDispatcher).dispatch(any(EmbeddingJob.class));

    assertThatThrownBy(() -> chunkEmbeddingService.triggerEmbedding(request))
        .isInstanceOf(ValidationException.class)
        .satisfies(ex -> assertThat(((ValidationException) ex).getCode()).isEqualTo("EMBEDDING_REJECTED"));
  }

  @Test
  void triggerEmbedding_secondDispatchFails_throwsValidationException() {
    request.setTeachingIds(List.of(1L, 2L));

    Teaching t1 = buildTeaching(1L, TeachingStatus.CHUNKED);
    Teaching t2 = buildTeaching(2L, TeachingStatus.CHUNKED);
    when(teachingRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(t1, t2));

    // First call succeeds, second fails
    doNothing().doThrow(new RejectedExecutionException())
        .when(embeddingJobDispatcher).dispatch(any(EmbeddingJob.class));

    assertThatThrownBy(() -> chunkEmbeddingService.triggerEmbedding(request))
        .isInstanceOf(ValidationException.class)
        .hasMessageContaining("Queue saturated");
  }

  // -----------------------------------------------------------------------
  // Helper methods
  // -----------------------------------------------------------------------

  private Teaching buildTeaching(Long id, TeachingStatus status) {
    Teaching teaching = Teaching.builder()
        .title("Test Teaching")
        .scripturalReferences("John 3:16")
        .message("Some message content")
        .summary("A summary")
        .audioUrl("https://example.com/audio.mp3")
        .videoUrl("https://example.com/video.mp4")
        .thumbnailUrl("https://example.com/thumb.jpg")
        .type(org.eni.koinoniadaily.modules.teaching.TeachingType.AUDIO)
        .tags("faith,love")
        .taughtAt(java.time.Instant.now())
        .status(status)
        .build();
    // Set id via reflection since @SuperBuilder won't allow it directly via builder
    try {
      java.lang.reflect.Field idField = org.eni.koinoniadaily.entity.BaseEntity.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(teaching, id);
    } catch (Exception e) {
      throw new RuntimeException("Failed to set id on Teaching", e);
    }
    return teaching;
  }
}