package org.eni.koinoniadaily.modules.chunkembedding;

import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.eni.koinoniadaily.infrastructure.embedding.models.EmbeddingModelProvider;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.TeachingStatus;
import org.eni.koinoniadaily.modules.teachingchunk.EmbeddingStatus;
import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunk;
import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbeddingPipelineServiceTest {

  @Mock
  private TeachingRepository teachingRepository;

  @Mock
  private TeachingChunkRepository teachingChunkRepository;

  @Mock
  private EmbeddingModelProvider embeddingModelProvider;

  @Mock
  private TransactionTemplate transactionTemplate;

  @InjectMocks
  private EmbeddingPipelineService service;

  private Teaching makeTeaching(Long id) {
    Teaching teaching = new Teaching();
    teaching.setId(id);
    teaching.setStatus(TeachingStatus.CHUNKED);
    return teaching;
  }

  private TeachingChunk makeChunk(Long id, String content) {
    TeachingChunk chunk = new TeachingChunk();
    chunk.setId(id);
    chunk.setContent(content);
    chunk.setEmbeddingStatus(EmbeddingStatus.PENDING);
    chunk.setChunkEmbeddings(new ArrayList<>());
    return chunk;
  }

  @BeforeEach
  void setUpTransactionTemplate() {
    // Make transactionTemplate.executeWithoutResult actually run the consumer.
    // Using lenient() to avoid strict-stubbing failures in tests that don't call executeWithoutResult.
    lenient().doAnswer(inv -> {
      Consumer<org.springframework.transaction.TransactionStatus> action = inv.getArgument(0);
      action.accept(mock(org.springframework.transaction.TransactionStatus.class));
      return null;
    }).when(transactionTemplate).executeWithoutResult(any());
  }

  @Nested
  class ProcessTests {

    @Test
    void process_teachingNotFound_doesNotThrowButLogsError() {
      // When teaching not found, NotFoundException is caught by outer try-catch
      // and updateStatus(FAILED) is attempted
      when(teachingRepository.findById(1L)).thenReturn(Optional.empty());

      // Should not propagate the exception (caught internally)
      service.process(new EmbeddingJob(1L));

      // Should attempt to mark as FAILED after the catastrophic failure
      verify(teachingRepository).updateStatus(eq(1L), eq(TeachingStatus.FAILED));
    }

    @Test
    void process_alreadyEmbedding_skipsProcessing() {
      Teaching teaching = makeTeaching(1L);
      when(teachingRepository.findById(1L)).thenReturn(Optional.of(teaching));
      // markAsEmbedding returns 0 meaning it was already EMBEDDING
      when(teachingRepository.markAsEmbedding(1L)).thenReturn(0);

      service.process(new EmbeddingJob(1L));

      // Should not process any chunks
      verifyNoInteractions(teachingChunkRepository);
      verifyNoInteractions(embeddingModelProvider);
    }

    @Test
    void process_successfulPath_resetsFailedChunksAndFinalizesStatus() {
      Teaching teaching = makeTeaching(1L);
      when(teachingRepository.findById(1L)).thenReturn(Optional.of(teaching));
      when(teachingRepository.markAsEmbedding(1L)).thenReturn(1);
      // No pending chunks on first call - pipeline exits immediately
      when(teachingChunkRepository.claimNextPendingChunks(eq(1L), anyInt()))
          .thenReturn(Collections.emptyList());

      service.process(new EmbeddingJob(1L));

      verify(teachingChunkRepository).resetFailedChunks(1L);
      verify(teachingRepository).finalizeEmbeddingStatus(1L);
    }

    @Test
    void process_singleBatch_embedsAndSavesChunks() {
      Teaching teaching = makeTeaching(1L);
      TeachingChunk chunk1 = makeChunk(10L, "Text one");
      TeachingChunk chunk2 = makeChunk(11L, "Text two");

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(teaching));
      when(teachingRepository.markAsEmbedding(1L)).thenReturn(1);
      when(embeddingModelProvider.getBatchSize()).thenReturn(20);
      when(teachingChunkRepository.claimNextPendingChunks(eq(1L), eq(20)))
          .thenReturn(List.of(chunk1, chunk2))
          .thenReturn(Collections.emptyList());

      float[] emb1 = new float[]{0.1f, 0.2f};
      float[] emb2 = new float[]{0.3f, 0.4f};
      when(embeddingModelProvider.embed(List.of("Text one", "Text two")))
          .thenReturn(List.of(emb1, emb2));
      when(embeddingModelProvider.getName()).thenReturn("test-model");

      service.process(new EmbeddingJob(1L));

      // Verify embeddings were saved via transaction
      verify(transactionTemplate).executeWithoutResult(any());
      // Chunks should have EMBEDDED status after successful embedding
      assertThat(chunk1.getEmbeddingStatus()).isEqualTo(EmbeddingStatus.EMBEDDED);
      assertThat(chunk2.getEmbeddingStatus()).isEqualTo(EmbeddingStatus.EMBEDDED);
      // ChunkEmbeddings should be appended
      assertThat(chunk1.getChunkEmbeddings()).hasSize(1);
      assertThat(chunk2.getChunkEmbeddings()).hasSize(1);
    }

    @Test
    void process_embeddingApiFails_marksChunksAsFailed() {
      Teaching teaching = makeTeaching(1L);
      TeachingChunk chunk = makeChunk(10L, "Content");

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(teaching));
      when(teachingRepository.markAsEmbedding(1L)).thenReturn(1);
      when(embeddingModelProvider.getBatchSize()).thenReturn(20);
      when(teachingChunkRepository.claimNextPendingChunks(eq(1L), eq(20)))
          .thenReturn(List.of(chunk))
          .thenReturn(Collections.emptyList());
      when(embeddingModelProvider.embed(any(List.class)))
          .thenThrow(new RuntimeException("API error"));

      service.process(new EmbeddingJob(1L));

      // Chunk should be marked FAILED
      assertThat(chunk.getEmbeddingStatus()).isEqualTo(EmbeddingStatus.FAILED);
      verify(teachingChunkRepository).saveAll(List.of(chunk));
      // Pipeline should still finalize status
      verify(teachingRepository).finalizeEmbeddingStatus(1L);
    }

    @Test
    void process_multipleBatches_processesAllBatches() {
      Teaching teaching = makeTeaching(1L);
      TeachingChunk chunk1 = makeChunk(10L, "Batch 1");
      TeachingChunk chunk2 = makeChunk(11L, "Batch 2");

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(teaching));
      when(teachingRepository.markAsEmbedding(1L)).thenReturn(1);
      when(embeddingModelProvider.getBatchSize()).thenReturn(20);
      when(teachingChunkRepository.claimNextPendingChunks(eq(1L), eq(20)))
          .thenReturn(List.of(chunk1))
          .thenReturn(List.of(chunk2))
          .thenReturn(Collections.emptyList());
      when(embeddingModelProvider.embed(any(List.class)))
          .thenReturn(List.of(new float[]{0.1f}))
          .thenReturn(List.of(new float[]{0.2f}));
      when(embeddingModelProvider.getName()).thenReturn("test-model");

      service.process(new EmbeddingJob(1L));

      // Two batches processed
      verify(embeddingModelProvider, times(2)).embed(any(List.class));
      verify(teachingRepository).finalizeEmbeddingStatus(1L);
    }

    @Test
    void process_catastrophicFailure_marksTeachingAsFailed() {
      Teaching teaching = makeTeaching(1L);
      when(teachingRepository.findById(1L)).thenReturn(Optional.of(teaching));
      when(teachingRepository.markAsEmbedding(1L)).thenReturn(1);
      // resetFailedChunks throws a catastrophic error
      doThrow(new RuntimeException("DB down")).when(teachingChunkRepository).resetFailedChunks(any());

      service.process(new EmbeddingJob(1L));

      verify(teachingRepository).updateStatus(eq(1L), eq(TeachingStatus.FAILED));
    }

    @Test
    void process_finalizeStatusCalledWithCorrectId() {
      Teaching teaching = makeTeaching(7L);
      when(teachingRepository.findById(7L)).thenReturn(Optional.of(teaching));
      when(teachingRepository.markAsEmbedding(7L)).thenReturn(1);
      when(teachingChunkRepository.claimNextPendingChunks(eq(7L), anyInt()))
          .thenReturn(Collections.emptyList());

      service.process(new EmbeddingJob(7L));

      verify(teachingRepository).finalizeEmbeddingStatus(7L);
    }

    @Test
    void process_chunkEmbeddingModelNameUsed() {
      Teaching teaching = makeTeaching(1L);
      TeachingChunk chunk = makeChunk(10L, "Content");

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(teaching));
      when(teachingRepository.markAsEmbedding(1L)).thenReturn(1);
      when(embeddingModelProvider.getBatchSize()).thenReturn(20);
      when(teachingChunkRepository.claimNextPendingChunks(eq(1L), eq(20)))
          .thenReturn(List.of(chunk))
          .thenReturn(Collections.emptyList());
      when(embeddingModelProvider.embed(any(List.class))).thenReturn(List.of(new float[]{0.1f}));
      when(embeddingModelProvider.getName()).thenReturn("text-embedding-3-small");

      service.process(new EmbeddingJob(1L));

      // Verify the model name was used when creating the ChunkEmbedding entity
      assertThat(chunk.getChunkEmbeddings()).isNotEmpty();
      assertThat(chunk.getChunkEmbeddings().get(0).getModel()).isEqualTo("text-embedding-3-small");
    }

    @Test
    void process_doubleFault_updateStatusAlsoFails_doesNotPropagateException() {
      when(teachingRepository.findById(1L)).thenReturn(Optional.empty());
      // The NotFoundException is caught by outer try-catch, then updateStatus also throws
      doThrow(new RuntimeException("Cannot update")).when(teachingRepository)
          .updateStatus(any(), any());

      // Should not propagate - double fault is silently absorbed
      service.process(new EmbeddingJob(1L));
    }
  }
}