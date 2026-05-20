package org.eni.koinoniadaily.modules.chunkembedding;

import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.eni.koinoniadaily.infrastructure.embedding.models.EmbeddingModelProvider;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.TeachingStatus;
import org.eni.koinoniadaily.modules.teaching.TeachingType;
import org.eni.koinoniadaily.modules.teachingchunk.EmbeddingStatus;
import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunk;
import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingPipelineServiceTest {

  @Mock
  private TeachingRepository teachingRepository;

  @Mock
  private TeachingChunkRepository teachingChunkRepository;

  @Mock
  private ChunkEmbeddingRepository chunkEmbeddingRepository;

  @Mock
  private EmbeddingModelProvider embeddingModelProvider;

  @Mock
  private TransactionTemplate transactionTemplate;

  @InjectMocks
  private EmbeddingPipelineService embeddingPipelineService;

  private static final Long TEACHING_ID = 1L;

  @BeforeEach
  void setUp() {
    // Make TransactionTemplate execute the callback synchronously in tests
    doAnswer(invocation -> {
      var consumer = invocation.getArgument(0, java.util.function.Consumer.class);
      consumer.accept(null);
      return null;
    }).when(transactionTemplate).executeWithoutResult(any());
  }

  // -----------------------------------------------------------------------
  // process() - teaching not found
  // -----------------------------------------------------------------------

  @Test
  void process_teachingNotFound_marksTeachingAsFailed() {
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.empty());

    EmbeddingJob job = new EmbeddingJob(TEACHING_ID);

    // Should catch the NotFoundException in the outer try/catch and mark as FAILED
    embeddingPipelineService.process(job);

    verify(teachingRepository).updateStatus(TEACHING_ID, TeachingStatus.FAILED);
  }

  // -----------------------------------------------------------------------
  // process() - already being processed (concurrent guard)
  // -----------------------------------------------------------------------

  @Test
  void process_teachingAlreadyBeingProcessed_marksTeachingAsFailed() {
    Teaching teaching = buildTeaching(TEACHING_ID, TeachingStatus.CHUNKED);
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.of(teaching));
    when(teachingRepository.markAsEmbedding(TEACHING_ID)).thenReturn(0); // 0 means already EMBEDDING

    EmbeddingJob job = new EmbeddingJob(TEACHING_ID);
    embeddingPipelineService.process(job);

    // The ValidationException is caught by outer catch; should mark FAILED
    verify(teachingRepository).updateStatus(TEACHING_ID, TeachingStatus.FAILED);
  }

  // -----------------------------------------------------------------------
  // process() - happy path: all chunks embedded successfully
  // -----------------------------------------------------------------------

  @Test
  void process_allChunksEmbedded_setsTeachingStatusToEmbedded() {
    Teaching teaching = buildTeaching(TEACHING_ID, TeachingStatus.CHUNKED);
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.of(teaching));
    when(teachingRepository.markAsEmbedding(TEACHING_ID)).thenReturn(1);
    when(embeddingModelProvider.getBatchSize()).thenReturn(2);

    // First call returns 2 chunks; second call returns empty (done)
    List<TeachingChunk> batch1 = List.of(
        buildChunk(1L, "Content A"),
        buildChunk(2L, "Content B")
    );
    when(teachingChunkRepository.claimNextPendingChunks(TEACHING_ID, 2))
        .thenReturn(batch1)
        .thenReturn(List.of());

    float[] emb1 = new float[]{0.1f, 0.2f};
    float[] emb2 = new float[]{0.3f, 0.4f};
    when(embeddingModelProvider.embed(anyList())).thenReturn(List.of(emb1, emb2));
    when(embeddingModelProvider.getName()).thenReturn("test-model");

    // No non-embedded chunks remain after processing
    when(teachingChunkRepository.existsByTeachingIdAndEmbeddingStatusNot(TEACHING_ID, EmbeddingStatus.EMBEDDED))
        .thenReturn(false);

    embeddingPipelineService.process(new EmbeddingJob(TEACHING_ID));

    assertThat(teaching.getStatus()).isEqualTo(TeachingStatus.EMBEDDED);
    verify(teachingRepository).save(teaching);
  }

  @Test
  void process_allChunksEmbedded_resetsFailedChunksFirst() {
    Teaching teaching = buildTeaching(TEACHING_ID, TeachingStatus.FAILED);
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.of(teaching));
    when(teachingRepository.markAsEmbedding(TEACHING_ID)).thenReturn(1);
    when(embeddingModelProvider.getBatchSize()).thenReturn(2);
    when(teachingChunkRepository.claimNextPendingChunks(TEACHING_ID, 2)).thenReturn(List.of());
    when(teachingChunkRepository.existsByTeachingIdAndEmbeddingStatusNot(TEACHING_ID, EmbeddingStatus.EMBEDDED))
        .thenReturn(false);

    embeddingPipelineService.process(new EmbeddingJob(TEACHING_ID));

    verify(teachingChunkRepository).resetFailedChunks(TEACHING_ID);
  }

  @Test
  void process_allChunksEmbedded_saveEmbeddingsCalledForEachBatch() {
    Teaching teaching = buildTeaching(TEACHING_ID, TeachingStatus.CHUNKED);
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.of(teaching));
    when(teachingRepository.markAsEmbedding(TEACHING_ID)).thenReturn(1);
    when(embeddingModelProvider.getBatchSize()).thenReturn(1);

    List<TeachingChunk> batch1 = List.of(buildChunk(1L, "Content A"));
    List<TeachingChunk> batch2 = List.of(buildChunk(2L, "Content B"));
    when(teachingChunkRepository.claimNextPendingChunks(TEACHING_ID, 1))
        .thenReturn(batch1)
        .thenReturn(batch2)
        .thenReturn(List.of());

    when(embeddingModelProvider.embed(anyList()))
        .thenReturn(List.of(new float[]{0.1f}))
        .thenReturn(List.of(new float[]{0.2f}));
    when(embeddingModelProvider.getName()).thenReturn("test-model");
    when(teachingChunkRepository.existsByTeachingIdAndEmbeddingStatusNot(TEACHING_ID, EmbeddingStatus.EMBEDDED))
        .thenReturn(false);

    embeddingPipelineService.process(new EmbeddingJob(TEACHING_ID));

    verify(chunkEmbeddingRepository, times(2)).saveAll(anyList());
    verify(teachingChunkRepository, times(2)).saveAll(anyList());
  }

  // -----------------------------------------------------------------------
  // process() - batch failure handling
  // -----------------------------------------------------------------------

  @Test
  void process_embeddingApiFails_marksChunksAsFailed() {
    Teaching teaching = buildTeaching(TEACHING_ID, TeachingStatus.CHUNKED);
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.of(teaching));
    when(teachingRepository.markAsEmbedding(TEACHING_ID)).thenReturn(1);
    when(embeddingModelProvider.getBatchSize()).thenReturn(2);

    TeachingChunk chunk1 = buildChunk(1L, "Content A");
    TeachingChunk chunk2 = buildChunk(2L, "Content B");
    when(teachingChunkRepository.claimNextPendingChunks(TEACHING_ID, 2))
        .thenReturn(List.of(chunk1, chunk2))
        .thenReturn(List.of());

    when(embeddingModelProvider.embed(anyList()))
        .thenThrow(new RuntimeException("OpenAI API error"));

    when(teachingChunkRepository.existsByTeachingIdAndEmbeddingStatusNot(TEACHING_ID, EmbeddingStatus.EMBEDDED))
        .thenReturn(true); // has failed chunks

    embeddingPipelineService.process(new EmbeddingJob(TEACHING_ID));

    // Chunks should be marked FAILED
    assertThat(chunk1.getEmbeddingStatus()).isEqualTo(EmbeddingStatus.FAILED);
    assertThat(chunk2.getEmbeddingStatus()).isEqualTo(EmbeddingStatus.FAILED);
    verify(teachingChunkRepository).saveAll(List.of(chunk1, chunk2));
  }

  @Test
  void process_embeddingApiFails_setsTeachingStatusToFailed() {
    Teaching teaching = buildTeaching(TEACHING_ID, TeachingStatus.CHUNKED);
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.of(teaching));
    when(teachingRepository.markAsEmbedding(TEACHING_ID)).thenReturn(1);
    when(embeddingModelProvider.getBatchSize()).thenReturn(2);

    when(teachingChunkRepository.claimNextPendingChunks(TEACHING_ID, 2))
        .thenReturn(List.of(buildChunk(1L, "Content")))
        .thenReturn(List.of());

    when(embeddingModelProvider.embed(anyList()))
        .thenThrow(new RuntimeException("API timeout"));

    when(teachingChunkRepository.existsByTeachingIdAndEmbeddingStatusNot(TEACHING_ID, EmbeddingStatus.EMBEDDED))
        .thenReturn(true);

    embeddingPipelineService.process(new EmbeddingJob(TEACHING_ID));

    assertThat(teaching.getStatus()).isEqualTo(TeachingStatus.FAILED);
  }

  @Test
  void process_someChunksFail_teachingStatusSetToFailed() {
    Teaching teaching = buildTeaching(TEACHING_ID, TeachingStatus.CHUNKED);
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.of(teaching));
    when(teachingRepository.markAsEmbedding(TEACHING_ID)).thenReturn(1);
    when(embeddingModelProvider.getBatchSize()).thenReturn(5);
    when(teachingChunkRepository.claimNextPendingChunks(TEACHING_ID, 5)).thenReturn(List.of());

    // Even with no more pending chunks, if existsByTeachingIdAndEmbeddingStatusNot is true,
    // teaching should be FAILED
    when(teachingChunkRepository.existsByTeachingIdAndEmbeddingStatusNot(TEACHING_ID, EmbeddingStatus.EMBEDDED))
        .thenReturn(true);

    embeddingPipelineService.process(new EmbeddingJob(TEACHING_ID));

    assertThat(teaching.getStatus()).isEqualTo(TeachingStatus.FAILED);
    verify(teachingRepository).save(teaching);
  }

  // -----------------------------------------------------------------------
  // process() - catastrophic failure during save
  // -----------------------------------------------------------------------

  @Test
  void process_catastrophicFailureDuringSave_updatesStatusToFailed() {
    Teaching teaching = buildTeaching(TEACHING_ID, TeachingStatus.CHUNKED);
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.of(teaching));
    when(teachingRepository.markAsEmbedding(TEACHING_ID)).thenReturn(1);
    when(embeddingModelProvider.getBatchSize()).thenReturn(1);

    when(teachingChunkRepository.claimNextPendingChunks(TEACHING_ID, 1))
        .thenReturn(List.of(buildChunk(1L, "Content")));

    when(embeddingModelProvider.embed(anyList()))
        .thenReturn(List.of(new float[]{0.1f}));
    when(embeddingModelProvider.getName()).thenReturn("test-model");

    // Simulate catastrophic failure during transaction
    doThrow(new RuntimeException("DB error"))
        .when(transactionTemplate).executeWithoutResult(any());

    embeddingPipelineService.process(new EmbeddingJob(TEACHING_ID));

    verify(teachingRepository).updateStatus(TEACHING_ID, TeachingStatus.FAILED);
  }

  @Test
  void process_catastrophicFailureAndUpdateStatusAlsoFails_doesNotThrow() {
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.empty());
    doThrow(new RuntimeException("DB completely down"))
        .when(teachingRepository).updateStatus(TEACHING_ID, TeachingStatus.FAILED);

    // Should swallow the double-fault and not propagate exception
    embeddingPipelineService.process(new EmbeddingJob(TEACHING_ID));

    // verify updateStatus was called
    verify(teachingRepository).updateStatus(TEACHING_ID, TeachingStatus.FAILED);
  }

  // -----------------------------------------------------------------------
  // process() - empty chunk list (no work to do)
  // -----------------------------------------------------------------------

  @Test
  void process_noChunksToProcess_setsTeachingEmbedded() {
    Teaching teaching = buildTeaching(TEACHING_ID, TeachingStatus.CHUNKED);
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.of(teaching));
    when(teachingRepository.markAsEmbedding(TEACHING_ID)).thenReturn(1);
    when(embeddingModelProvider.getBatchSize()).thenReturn(5);
    when(teachingChunkRepository.claimNextPendingChunks(TEACHING_ID, 5)).thenReturn(List.of());

    // No non-embedded chunks
    when(teachingChunkRepository.existsByTeachingIdAndEmbeddingStatusNot(TEACHING_ID, EmbeddingStatus.EMBEDDED))
        .thenReturn(false);

    embeddingPipelineService.process(new EmbeddingJob(TEACHING_ID));

    assertThat(teaching.getStatus()).isEqualTo(TeachingStatus.EMBEDDED);
  }

  @Test
  void process_embeddingComplete_savesCombinedEmbeddingsAndChunks() {
    Teaching teaching = buildTeaching(TEACHING_ID, TeachingStatus.CHUNKED);
    when(teachingRepository.findById(TEACHING_ID)).thenReturn(Optional.of(teaching));
    when(teachingRepository.markAsEmbedding(TEACHING_ID)).thenReturn(1);
    when(embeddingModelProvider.getBatchSize()).thenReturn(3);

    TeachingChunk c1 = buildChunk(1L, "Text A");
    TeachingChunk c2 = buildChunk(2L, "Text B");
    TeachingChunk c3 = buildChunk(3L, "Text C");

    when(teachingChunkRepository.claimNextPendingChunks(TEACHING_ID, 3))
        .thenReturn(List.of(c1, c2, c3))
        .thenReturn(List.of());

    when(embeddingModelProvider.embed(List.of("Text A", "Text B", "Text C")))
        .thenReturn(List.of(new float[]{0.1f}, new float[]{0.2f}, new float[]{0.3f}));
    when(embeddingModelProvider.getName()).thenReturn("test-model");
    when(teachingChunkRepository.existsByTeachingIdAndEmbeddingStatusNot(TEACHING_ID, EmbeddingStatus.EMBEDDED))
        .thenReturn(false);

    embeddingPipelineService.process(new EmbeddingJob(TEACHING_ID));

    // Verify chunks are marked as EMBEDDED
    assertThat(c1.getEmbeddingStatus()).isEqualTo(EmbeddingStatus.EMBEDDED);
    assertThat(c2.getEmbeddingStatus()).isEqualTo(EmbeddingStatus.EMBEDDED);
    assertThat(c3.getEmbeddingStatus()).isEqualTo(EmbeddingStatus.EMBEDDED);
  }

  // -----------------------------------------------------------------------
  // Helper methods
  // -----------------------------------------------------------------------

  private Teaching buildTeaching(Long id, TeachingStatus status) {
    Teaching teaching = Teaching.builder()
        .title("Test Teaching")
        .scripturalReferences("John 3:16")
        .message("Message content here for chunking")
        .summary("Summary")
        .audioUrl("https://example.com/audio.mp3")
        .videoUrl("https://example.com/video.mp4")
        .thumbnailUrl("https://example.com/thumb.jpg")
        .type(TeachingType.AUDIO)
        .tags("faith")
        .taughtAt(Instant.now())
        .status(status)
        .build();
    setId(teaching, id);
    return teaching;
  }

  private TeachingChunk buildChunk(Long id, String content) {
    TeachingChunk chunk = TeachingChunk.builder()
        .content(content)
        .chunkIndex(id.intValue())
        .embeddingStatus(EmbeddingStatus.PENDING)
        .chunkEmbeddings(new ArrayList<>())
        .build();
    setId(chunk, id);
    return chunk;
  }

  private void setId(Object entity, Long id) {
    try {
      java.lang.reflect.Field idField = org.eni.koinoniadaily.entity.BaseEntity.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(entity, id);
    } catch (Exception e) {
      throw new RuntimeException("Failed to set id", e);
    }
  }
}