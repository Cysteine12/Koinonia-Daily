package org.eni.koinoniadaily.infrastructure.embedding.models.textembedding3small;

import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.config.AppProperties;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.embedding.model.provider", havingValue = "text-embedding-3-small")
@RequiredArgsConstructor
public class TextEmbedding3SmallEmbeddingModelConfig {

  private final AppProperties appProperties;

  @Bean
  EmbeddingModel embeddingModel() {
    OpenAiEmbeddingOptions options = OpenAiEmbeddingOptions.builder()
        .apiKey(appProperties.getOpenaiApiKey())
        .model(TextEmbedding3Small.getName())
        .dimensions(TextEmbedding3Small.getDimensions())
        .build();

    return new OpenAiEmbeddingModel(options);
  }
}
