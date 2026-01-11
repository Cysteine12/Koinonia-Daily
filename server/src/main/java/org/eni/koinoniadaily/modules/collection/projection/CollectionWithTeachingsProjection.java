package org.eni.koinoniadaily.modules.collection.projection;

import java.time.LocalDateTime;
import java.util.List;

import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;

public interface CollectionWithTeachingsProjection {
  
  Long getId();

  String getName();

  String getDescription();

  String getThumbnailUrl();

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();

  List<TeachingWithoutMessageProjection> getTeachings();
}
