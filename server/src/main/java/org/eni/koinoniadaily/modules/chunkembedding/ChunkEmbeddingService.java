package org.eni.koinoniadaily.modules.chunkembedding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.infrastructure.embedding.dispatchers.EmbeddingJobDispatcher;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.eni.koinoniadaily.modules.chunkembedding.dto.ChunkEmbeddingRequest;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.TeachingStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChunkEmbeddingService {

  private final EmbeddingJobDispatcher embeddingJobDispatcher;
  private final TeachingRepository teachingRepository;

  public void triggerEmbedding(ChunkEmbeddingRequest request) {

    log.info("Embedding trigger initiated for teaching: {}", request.getTeachingIds().toString());

    List<Teaching> teachings = teachingRepository.findAllById(request.getTeachingIds());

    for (Teaching teaching : teachings) {
      if (teaching.getStatus() == TeachingStatus.PENDING) {
        throw new ValidationException("Teaching with id " + teaching.getId() + " has not been chunked");
      }
    }

    for (Teaching teaching : teachings) {
      try {
        embeddingJobDispatcher.dispatch(new EmbeddingJob(teaching.getId()));
      } catch(RejectedExecutionException ex) {
        throw new ValidationException("EMBEDDING_REJECTED", "Queue saturated. Retry later");
      }
    }

    log.info("Embedding trigger successful for teachings: {}", teachings.stream().map(Teaching::getId).toList());
  }
}
