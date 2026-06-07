package org.eni.koinoniadaily.modules.recommendation;

import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.modules.recommendation.dto.RecommendationResponse;
import org.eni.koinoniadaily.modules.recommendation.projection.RecommendationResult;
import org.eni.koinoniadaily.modules.teachingembedding.TeachingEmbedding;
import org.eni.koinoniadaily.modules.teachingembedding.TeachingEmbeddingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

  private final TeachingEmbeddingRepository teachingEmbeddingRepository;


  public List<RecommendationResponse> getRecommendations(Long teachingId, int size) {

    TeachingEmbedding teachingEmbedding = teachingEmbeddingRepository.findByTeachingId(teachingId)
        .orElseThrow(() -> new NotFoundException("Teaching embedding not found"));

    List<RecommendationResult> similarTeachings = teachingEmbeddingRepository.getSimilarTeachings(
        teachingEmbedding.getEmbedding(),
        teachingEmbedding.getModel(),
        teachingId,
        size
    );

    return similarTeachings.stream()
        .map(this::toDto)
        .toList();
  }

  private RecommendationResponse toDto(RecommendationResult result) {

    return RecommendationResponse.builder()
        .id(result.getId())
        .title(result.getTitle())
        .summary(result.getSummary())
        .thumbnailUrl(result.getThumbnailUrl())
        .type(result.getType())
        .seriesPart(result.getSeriesPart())
        .taughtAt(result.getTaughtAt())
        .createdAt(result.getCreatedAt())
        .updatedAt(result.getUpdatedAt())
        .signal(RecommendationSignal.EMBEDDING)
        .build();
  }
}
