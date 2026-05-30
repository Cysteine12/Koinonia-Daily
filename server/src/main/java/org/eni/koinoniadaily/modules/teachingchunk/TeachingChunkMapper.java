package org.eni.koinoniadaily.modules.teachingchunk;

import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teachingchunk.dto.ChunkCandidate;
import org.springframework.stereotype.Component;

@Component
public class TeachingChunkMapper {

  public TeachingChunk candidateToEntity(ChunkCandidate candidate, Teaching teaching) {

    return TeachingChunk.builder()
        .teaching(teaching)
        .chunkIndex(candidate.chunkIndex())
        .teachingTitle(teaching.getTitle())
        .sectionTitle(candidate.sectionTitle())
        .content(candidate.content())
        .embeddingStatus(EmbeddingStatus.PENDING)
        .startOffset(candidate.startOffset())
        .build();
  }
}
