package org.eni.koinoniadaily.modules.chunkembedding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.eni.koinoniadaily.infrastructure.embedding.models.EmbeddingModelProvider;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.TeachingStatus;
import org.eni.koinoniadaily.modules.teachingchunk.EmbeddingStatus;
import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunk;
import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingPipelineService {

  private final TeachingRepository teachingRepository;
  private final TeachingChunkRepository teachingChunkRepository;
  private final ChunkEmbeddingRepository chunkEmbeddingRepository;
  private final EmbeddingModelProvider embeddingModelProvider;
  private final TransactionTemplate transactionTemplate;

  public void process(EmbeddingJob job) {

    log.info("Embedding Pipeline started for teaching {}", job.teachingId());

    Teaching teaching = this.findByIdAndMarkAsEmbedding(job.teachingId());

    teachingChunkRepository.resetFailedChunks(job.teachingId());

    try {
      while (true) {
        List<TeachingChunk> chunks = teachingChunkRepository.claimNextPendingChunks(
            job.teachingId(),
            embeddingModelProvider.getBatchSize()
        );

        if (chunks.isEmpty()) {
          break;
        }

        try {
          List<String> chunkContents = chunks.stream().map(TeachingChunk::getContent).toList();

          List<float[]> embeddings = embeddingModelProvider.embed(chunkContents);

          transactionTemplate.executeWithoutResult(_ ->
              this.saveEmbeddingsAndUpdateChunksStatus(chunks, embeddings)
          );
        } catch (RuntimeException ex) {

          log.error("Embedding failed for teaching chunk batch ranging {}",
              chunks.stream()
                  .map(BaseEntity::getId)
                  .toList());

          for (TeachingChunk teachingChunk : chunks) {

            teachingChunk.setEmbeddingStatus(EmbeddingStatus.FAILED);
          }
          teachingChunkRepository.saveAll(chunks);
        }
      }

      boolean hasAnyNonEmbeddedChunks = teachingChunkRepository.existsByTeachingIdAndEmbeddingStatusNot(
          teaching.getId(),
          EmbeddingStatus.EMBEDDED
      );


      teaching.setStatus(hasAnyNonEmbeddedChunks ? TeachingStatus.FAILED : TeachingStatus.EMBEDDED);
      teachingRepository.save(teaching);

      log.info("Embedding Pipeline completed for teaching {}", job.teachingId());
    } catch (RuntimeException ex) {

      log.error("Catastrophic failure in embedding pipeline for teaching {}", job.teachingId(), ex);

      try {
        teachingRepository.updateStatus(job.teachingId(), TeachingStatus.FAILED);
      } catch (RuntimeException e) {
        log.error("Double-fault: Could not even mark teaching as FAILED", e);
      }
    }
  }

  private Teaching findByIdAndMarkAsEmbedding(Long id) {

    Teaching teaching = teachingRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Teaching not found"));

    int updatedRows = teachingRepository.markAsEmbedding(id);

    // GUARD: If rows == 0, it means it was already 'EMBEDDING'
    if (updatedRows == 0) {
      log.warn("Teaching {} is already being processed. Skipping.", id);
      throw new ValidationException("Teaching is already being processed");
    }

    return teaching;
  }

  private void saveEmbeddingsAndUpdateChunksStatus(
      List<TeachingChunk> chunks, List<float[]> embeddings
  ) {

    List<ChunkEmbedding> chunkEmbeddings = new ArrayList<>();

    for (int j = 0; j < chunks.size(); j++) {

      TeachingChunk chunk = chunks.get(j);

      ChunkEmbedding embedding = this.toEntity(embeddings.get(j), chunk);

      List<ChunkEmbedding> list = Stream.concat(
          chunk.getChunkEmbeddings().stream(),
          Stream.of(embedding)
      ).toList();
      chunk.setChunkEmbeddings(list);
      chunk.setEmbeddingStatus(EmbeddingStatus.EMBEDDED);

      chunkEmbeddings.add(embedding);
    }

    chunkEmbeddingRepository.saveAll(chunkEmbeddings);

    teachingChunkRepository.saveAll(chunks);
  }

  private ChunkEmbedding toEntity(float[] embedding, TeachingChunk teachingChunk) {

    return ChunkEmbedding.builder()
        .teachingChunk(teachingChunk)
        .model(embeddingModelProvider.getName())
        .embedding(embedding)
        .build();
  }
}
