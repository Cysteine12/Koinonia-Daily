package org.eni.koinoniadaily.modules.collection;

import java.util.List;

import org.eni.koinoniadaily.modules.collection.dto.CollectionRequest;
import org.eni.koinoniadaily.modules.collection.dto.CollectionResponse;
import org.eni.koinoniadaily.modules.collection.projection.CollectionWithTeachingsProjection;
import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;
import org.springframework.stereotype.Component;

@Component
public class CollectionMapper {
  
  public CollectionResponse toDto(CollectionWithTeachingsProjection collection) {

    return CollectionResponse.builder()
            .id(collection.getId())
            .name(collection.getName())
            .description(collection.getDescription())
            .thumbnailUrl(collection.getThumbnailUrl())
            .createdAt(collection.getCreatedAt())
            .updatedAt(collection.getUpdatedAt())
            .teachings(collection.getTeachings())
            .build();
  }

  public CollectionResponse toDto(Collection collection, List<TeachingWithoutMessageProjection> teachings) {
    
    return CollectionResponse.builder()
            .id(collection.getId())
            .name(collection.getName())
            .description(collection.getDescription())
            .thumbnailUrl(collection.getThumbnailUrl())
            .createdAt(collection.getCreatedAt())
            .updatedAt(collection.getUpdatedAt())
            .teachings(teachings)
            .build();
  }

  public Collection toEntity(CollectionRequest request) {
    
    return Collection.builder()
            .name(request.getName())
            .description(request.getDescription())
            .thumbnailUrl(request.getThumbnailUrl())
            .build();
  }

  public Collection toEntity(Long id, CollectionRequest request) {
    
    return Collection.builder()
            .id(id)
            .name(request.getName())
            .description(request.getDescription())
            .thumbnailUrl(request.getThumbnailUrl())
            .build();
  }
}
