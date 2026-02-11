package org.eni.koinoniadaily.modules.collection.projection;

import java.time.Instant;
import java.util.List;

import org.eni.koinoniadaily.modules.teaching.projection.TeachingWithoutMessageProjection;

public interface CollectionWithTeachingsProjection {
  
  Long getId();

  String getName();

  String getDescription();

  String getThumbnailUrl();

  Instant getCreatedAt();

  Instant getUpdatedAt();

  List<TeachingWithoutMessageProjection> getTeachings();
}
