package org.eni.koinoniadaily.infrastructure.embedding.models.textembedding3small;


import lombok.Getter;

final class TextEmbedding3Small {

  @Getter
  private static final String name = "text-embedding-3-small";

  @Getter
  private static final int dimensions = 1536;

  @Getter
  private static final int batchSize = 20;
}
