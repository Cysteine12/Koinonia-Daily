package org.eni.koinoniadaily.modules.search;

import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.infrastructure.embedding.models.EmbeddingModelProvider;
import org.eni.koinoniadaily.modules.search.dto.SearchResponse;
import org.eni.koinoniadaily.modules.search.projection.SearchResult;
import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunkRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final TeachingChunkRepository teachingChunkRepository;
  private final EmbeddingModelProvider embeddingModelProvider;

  public List<SearchResponse> search(String query, int page, int limit, boolean semantic) {

    if (semantic) {
      return this.searchWithEmbedding(query, page, limit);
    }
    return this.searchWithoutEmbedding(query, page, limit);
  }

  private List<SearchResponse> searchWithEmbedding(String query, int page, int limit) {

    float[] queryEmbedding = embeddingModelProvider.embed(query);

    String model = embeddingModelProvider.getName();

    List<SearchResult> searchResults = teachingChunkRepository.search(query, queryEmbedding, model, page * limit, limit);

    return searchResults.stream().map(this::toDto).toList();
  }

  private List<SearchResponse> searchWithoutEmbedding(String query, int page, int limit) {

    List<SearchResult> searchResults = teachingChunkRepository.search(query, page * limit, limit);

    return searchResults.stream().map(this::toDto).toList();
  }

  private SearchResponse toDto(SearchResult result) {

    return SearchResponse.builder()
        .teachingId(result.getTeachingId())
        .chunkId(result.getChunkId())
        .teachingTitle(result.getTeachingTitle())
        .sectionTitle(result.getSectionTitle())
        .content(result.getContent())
        .chunkIndex(result.getChunkIndex())
        .thumbnailUrl(result.getThumbnailUrl())
        .type(result.getType())
        .taughtAt(result.getTaughtAt())
        .score(result.getScore())
        .matchSources(Arrays.asList(result.getMatchSources().split(",")))
        .build();
  }
}
