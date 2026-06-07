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

  public final TeachingEmbeddingRepository teachingEmbeddingRepository;
  private final TeachingEmbeddingUtil teachingEmbeddingUtil;
  public final EmbeddingModelProvider embeddingModelProvider;
  public final TeachingRepository teachingRepository;

  public void triggerEmbedding (TeachingEmbeddingRequest request) {

    Teaching teaching = teachingRepository.findById(request.getTeachingId())
        .orElseThrow(() -> new NotFoundException("Teaching not found"));

    String text = teachingEmbeddingUtil.buildChunkText(teaching);

    float[] embedding = embeddingModelProvider.embed(text);

    teachingEmbeddingRepository.save(toEntity(teaching, embedding));
  }

  private TeachingEmbedding toEntity(Teaching teaching, float[] embedding) {

    return TeachingEmbedding.builder()
        .teaching(teaching)
        .model(embeddingModelProvider.getName())
        .embedding(embedding)
        .build();
  }
}