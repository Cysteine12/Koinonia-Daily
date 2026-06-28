package org.eni.koinoniadaily.modules.search;

import lombok.RequiredArgsConstructor;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.infrastructure.embedding.models.EmbeddingModelProvider;
import org.eni.koinoniadaily.modules.auth.CurrentUserProvider;
import org.eni.koinoniadaily.modules.search.dto.SearchClickRequest;
import org.eni.koinoniadaily.modules.search.dto.SearchClickResponse;
import org.eni.koinoniadaily.modules.search.dto.SearchResponse;
import org.eni.koinoniadaily.modules.search.dto.SearchSuggestionResponse;
import org.eni.koinoniadaily.modules.search.projection.RecentSearchClick;
import org.eni.koinoniadaily.modules.search.projection.SearchResult;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.projection.TeachingTitleSuggestionProjection;
import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunkRepository;
import org.eni.koinoniadaily.modules.user.User;
import org.eni.koinoniadaily.modules.user.UserRepository;
import org.eni.koinoniadaily.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

  private final TeachingChunkRepository teachingChunkRepository;
  private final EmbeddingModelProvider embeddingModelProvider;
  private final TeachingRepository teachingRepository;
  private final SearchClickRepository searchClickRepository;
  private final CurrentUserProvider currentUserProvider;
  private final UserRepository userRepository;

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

  public List<SearchSuggestionResponse> suggest(String query, int page, int size) {
    
    return teachingRepository.findTitleSuggestion(query, page * size, size)
        .stream()
        .map(this::toSuggestionDto)
        .toList();
  }

  public PageResponse<SearchClickResponse> getRecentClicks(int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

    Long userId = currentUserProvider.getCurrentUserId();

    Page<RecentSearchClick> searchClicks = searchClickRepository.findAllByUserId(userId, pageable);

    return PageResponse.from(searchClicks.map(this::toSearchClickDto));
  }
  @Transactional
  public void recordClick(SearchClickRequest request) {

    if(!teachingRepository.existsById(request.getTeachingId())) {
        throw new NotFoundException("Teaching not found");
    }

    User user = userRepository.getReferenceById(currentUserProvider.getCurrentUserId());

    searchClickRepository.upsertByTeachingIdAndUserId(request.getTeachingId(), user.getId(), Instant.now());
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
        .matchSources(result.getMatchSources() != null
            ? Arrays.asList(result.getMatchSources().split(","))
            : List.of())
        .build();
  }

  private SearchSuggestionResponse toSuggestionDto(TeachingTitleSuggestionProjection projection) {
    
    return SearchSuggestionResponse.builder()
        .id(projection.getId())
        .title(projection.getTitle())
        .thumbnailUrl(projection.getThumbnailUrl())
        .type(projection.getType())
        .taughtAt(projection.getTaughtAt())
        .build();
  }

  private SearchClickResponse toSearchClickDto(RecentSearchClick click) {
    
    return SearchClickResponse.builder()
        .id(click.getId())
        .teachingId(click.getTeachingId())
        .title(click.getTitle())
        .thumbnailUrl(click.getThumbnailUrl())
        .type(click.getType())
        .taughtAt(click.getTaughtAt())
        .build();
  }
}
