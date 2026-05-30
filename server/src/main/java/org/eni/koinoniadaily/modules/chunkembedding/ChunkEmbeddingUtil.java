package org.eni.koinoniadaily.modules.chunkembedding;

import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunk;
import org.springframework.stereotype.Component;

@Component
public class ChunkEmbeddingUtil {

  public String buildChunkText(TeachingChunk chunk) {

    return """
        Title: %s
        
        Section: %s
        
        Content:
        %s
        """.formatted(chunk.getTeachingTitle(), chunk.getSectionTitle(), chunk.getContent());
  }
}
