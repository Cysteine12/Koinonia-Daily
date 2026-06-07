package org.eni.koinoniadaily.modules.teachingembedding;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.infrastructure.embedding.models.EmbeddingModelProvider;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teachingembedding.dto.TeachingEmbeddingRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeachingEmbeddingService {

  private final TeachingEmbeddingRepository teachingEmbeddingRepository;
  private final TeachingEmbeddingUtil teachingEmbeddingUtil;
  private final EmbeddingModelProvider embeddingModelProvider;
  private final TeachingRepository teachingRepository;

  public void triggerEmbedding (TeachingEmbeddingRequest request) {

    Teaching teaching = teachingRepository.findById(request.getTeachingId())
        .orElseThrow(() -> new NotFoundException("Teaching not found"));

    String text = teachingEmbeddingUtil.buildChunkText(teaching);

    float[] embedding = embeddingModelProvider.embed(text);
    String model = embeddingModelProvider.getName();

    TeachingEmbedding teachingEmbedding = teachingEmbeddingRepository.findByTeachingIdAndModel(teaching.getId(), model)
        .orElseGet(() -> TeachingEmbedding.builder()
            .teaching(teaching)
            .model(model)
            .build()
        );

    teachingEmbedding.setEmbedding(embedding);
    teachingEmbeddingRepository.save(teachingEmbedding);
  }
}