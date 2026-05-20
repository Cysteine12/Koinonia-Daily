package org.eni.koinoniadaily.infrastructure.embedding.models.textembedding3small;

import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.infrastructure.embedding.models.EmbeddingModelProvider;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "app.embedding.model.provider", havingValue = "text-embedding-3-small")
@Qualifier("text-embedding-3-small")
@RequiredArgsConstructor
public class TextEmbedding3SmallEmbeddingModelProvider implements EmbeddingModelProvider {

  private final EmbeddingModel embeddingModel;

  @Override
  public String getName() {
    return TextEmbedding3Small.getName();
  }

  @Override
  public int getBatchSize() {
    return TextEmbedding3Small.getBatchSize();
  }

  @Override
  @Retryable(
      includes = Exception.class,
      maxRetries = 2,
      delay = 1000,
      multiplier = 2,
      maxDelay = 5000,
      jitter = 500
  )
  public float[] embed(String text) {
    return embeddingModel.embed(text);
  }

  @Override
  @Retryable(
      includes = Exception.class,
      maxRetries = 3,
      delay = 2000,
      multiplier = 2,
      maxDelay = 10000,
      jitter = 500
  )
  public List<float[]> embed(List<String> texts) {

    EmbeddingResponse response = embeddingModel.embedForResponse(texts);

    return response.getResults()
        .stream()
        .map(Embedding::getOutput)
        .toList();
  }
}
