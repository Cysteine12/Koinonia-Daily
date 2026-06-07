package org.eni.koinoniadaily.modules.chunkembedding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.infrastructure.embedding.dispatchers.EmbeddingJobDispatcher;
import org.eni.koinoniadaily.infrastructure.embedding.dto.EmbeddingJob;
import org.eni.koinoniadaily.modules.chunkembedding.dto.ChunkEmbeddingRequest;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.EmbeddingStatus;
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

    log.info("Embedding trigger initiated for teachings: {}", request.getTeachingIds());

    List<Teaching> teachings = teachingRepository.findAllById(request.getTeachingIds());

    if (teachings.size() != request.getTeachingIds().size()) {
      log.error("Embedding trigger failed due to invalid id for teachings: {}", request.getTeachingIds());

      throw new ValidationException("Invalid teaching id supplied. Try again");
    }

    for (Teaching teaching : teachings) {
      if (teaching.getEmbeddingStatus() == EmbeddingStatus.PENDING) {
        log.error("Embedding trigger failed with un-chunked teaching for teachings: {}", request.getTeachingIds());

        throw new ValidationException("Teaching with id " + teaching.getId() + " has not been chunked");
      }
    }

    for (Teaching teaching : teachings) {
      try {
        embeddingJobDispatcher.dispatch(new EmbeddingJob(teaching.getId()));
      } catch(RejectedExecutionException ex) {
        log.error("Embedding trigger rejected with queue saturation for teachings: {}", request.getTeachingIds());

        throw new ValidationException("EMBEDDING_REJECTED", "Queue saturated. Retry later");
      }
    }

    log.info("Embedding trigger successful for teachings: {}", teachings.stream().map(Teaching::getId).toList());
  }
}
