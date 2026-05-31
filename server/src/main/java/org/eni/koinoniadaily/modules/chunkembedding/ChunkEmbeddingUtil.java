package org.eni.koinoniadaily.modules.chunkembedding;

import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunk;
import org.springframework.stereotype.Component;

@Component
public class ChunkEmbeddingUtil {

  public String buildChunkText(TeachingChunk chunk) {

    StringBuilder sb = new StringBuilder();

    sb.append("Title: ").append(chunk.getTeachingTitle()).append("\n\n");

    if (chunk.getSectionTitle() != null && !chunk.getSectionTitle().isBlank()) {
      sb.append("Section: ").append(chunk.getSectionTitle()).append("\n\n");
    }

    sb.append("Content:\n").append(chunk.getContent());

    return sb.toString();
  }
}
