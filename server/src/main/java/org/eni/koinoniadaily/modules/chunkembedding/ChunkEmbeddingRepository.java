package org.eni.koinoniadaily.modules.chunkembedding;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunkEmbeddingRepository extends JpaRepository<ChunkEmbedding, Long> {
}
