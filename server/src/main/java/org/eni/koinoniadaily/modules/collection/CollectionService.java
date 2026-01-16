package org.eni.koinoniadaily.modules.collection;

import java.util.Collections;
import java.util.List;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.modules.collection.dto.CollectionPageResponse;
import org.eni.koinoniadaily.modules.collection.dto.CollectionRequest;
import org.eni.koinoniadaily.modules.collection.dto.CollectionResponse;
import org.eni.koinoniadaily.modules.collection.dto.CollectionTeachingRequest;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;
import org.eni.koinoniadaily.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollectionService {
  
  private final CollectionRepository collectionRepository;
  private final TeachingRepository teachingRepository;
  private final CollectionMapper collectionMapper;
  private static final String UPDATED_AT = "updatedAt";

  public PageResponse<CollectionPageResponse> getCollections(int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, UPDATED_AT));

    Page<CollectionPageResponse> collections = collectionRepository.findAllBy(pageable);

    return PageResponse.from(collections);
  }

  public CollectionResponse getCollectionById(Long id) {

    return collectionRepository.findProjectedById(id)
            .map(collectionMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Collection not found"));
  }

  @Transactional
  public CollectionResponse createCollection(CollectionRequest request) {

    Collection collection = collectionMapper.toEntity(request);

    Collection savedCollection = collectionRepository.save(collection);

    return collectionMapper.toDto(savedCollection, Collections.emptyList());
  }

  @Transactional
  public CollectionResponse updateCollection(Long id, CollectionRequest request) {

    Collection collection = collectionRepository.findById(id)
                              .orElseThrow(() -> new NotFoundException("Collection not found"));

    collection.setName(request.getName());
    collection.setDescription(request.getDescription());
    collection.setThumbnailUrl(request.getThumbnailUrl());

    List<TeachingWithoutMessageProjection> teachings = teachingRepository.findAllByCollectionsId(id);
    
    return collectionMapper.toDto(collection, teachings);
  }

  @Transactional
  public void addCollectionTeaching(Long id, CollectionTeachingRequest request) {

    Collection collection = collectionRepository.findById(id)
                              .orElseThrow(() -> new NotFoundException("Collection not found"));

    Teaching teaching = teachingRepository.findById(request.getTeachingId())
                          .orElseThrow(() -> new NotFoundException("Teaching not found"));

    collection.addTeaching(teaching);
  }

  @Transactional
  public void removeCollectionTeaching(Long id, Long teachingId) {

    Collection collection = collectionRepository.findById(id)
                              .orElseThrow(() -> new NotFoundException("Collection not found"));

    Teaching teaching = teachingRepository.findById(teachingId)
                          .orElseThrow(() -> new NotFoundException("Teaching not found"));

    collection.removeTeaching(teaching);
  }

  @Transactional
  public void deleteCollection(Long id) {

    Collection collection = collectionRepository.findById(id)
                      .orElseThrow(() -> new NotFoundException("Collection not found"));

    collectionRepository.delete(collection);
  }
}
