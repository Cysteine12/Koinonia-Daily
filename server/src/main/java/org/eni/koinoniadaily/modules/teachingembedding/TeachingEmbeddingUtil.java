package org.eni.koinoniadaily.modules.teachingembedding;

import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.springframework.stereotype.Component;

@Component
public class TeachingEmbeddingUtil {

  public String buildChunkText(Teaching teaching) {
    return "Title: %s%nSummary: %s%nTags: %s%n".formatted(
        teaching.getTitle(),
        teaching.getSummary(),
        teaching.getTags()
    );
  }
}
