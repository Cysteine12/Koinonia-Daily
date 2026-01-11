package org.eni.koinoniadaily.modules.collection;

import java.util.Optional;

import org.eni.koinoniadaily.modules.collection.dto.CollectionPageResponse;
import org.eni.koinoniadaily.modules.collection.projection.CollectionWithTeachingsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {

  @Query("SELECT new org.eni.koinoniadaily.modules.collection.dto.CollectionPageResponse(" +
        "c.id, c.name, c.description, c.thumbnailUrl, c.createdAt, c.updatedAt, SIZE(c.teachings)) " +
        "FROM Collection c")
  Page<CollectionPageResponse> findAllBy(Pageable pageable);

  Optional<CollectionWithTeachingsProjection> findProjectedById(Long id);
}
