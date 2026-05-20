package org.eni.koinoniadaily.config;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the Embedding configuration class added in this PR,
 * including its validation constraint isTokenWindowValid().
 */
class AppPropertiesEmbeddingTest {

  private AppProperties.Embedding buildEmbedding(int maxChunkToken, int overlapToken) {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setMaxChunkToken(maxChunkToken);
    embedding.setOverlapToken(overlapToken);
    return embedding;
  }

  // =========================================================================
  // Defaults
  // =========================================================================

  @Test
  void embedding_defaultMaxChunkToken_is1200() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    assertThat(embedding.getMaxChunkToken()).isEqualTo(1200);
  }

  @Test
  void embedding_defaultOverlapToken_is150() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    assertThat(embedding.getOverlapToken()).isEqualTo(150);
  }

  @Test
  void embedding_defaults_passTokenWindowValidation() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    assertThat(embedding.isTokenWindowValid()).isTrue();
  }

  // =========================================================================
  // isTokenWindowValid() tests
  // =========================================================================

  @Nested
  class TokenWindowValidationTests {

    @Test
    void isTokenWindowValid_overlapLessThanMax_returnsTrue() {
      AppProperties.Embedding embedding = buildEmbedding(1000, 100);
      assertThat(embedding.isTokenWindowValid()).isTrue();
    }

    @Test
    void isTokenWindowValid_overlapEqualToMax_returnsFalse() {
      AppProperties.Embedding embedding = buildEmbedding(500, 500);
      assertThat(embedding.isTokenWindowValid()).isFalse();
    }

    @Test
    void isTokenWindowValid_overlapGreaterThanMax_returnsFalse() {
      AppProperties.Embedding embedding = buildEmbedding(100, 200);
      assertThat(embedding.isTokenWindowValid()).isFalse();
    }

    @Test
    void isTokenWindowValid_zeroOverlap_returnsTrue() {
      AppProperties.Embedding embedding = buildEmbedding(1200, 0);
      assertThat(embedding.isTokenWindowValid()).isTrue();
    }

    @Test
    void isTokenWindowValid_overlapOneLessThanMax_returnsTrue() {
      AppProperties.Embedding embedding = buildEmbedding(100, 99);
      assertThat(embedding.isTokenWindowValid()).isTrue();
    }

    @Test
    void isTokenWindowValid_overlapOneMoreThanMax_returnsFalse() {
      AppProperties.Embedding embedding = buildEmbedding(100, 101);
      assertThat(embedding.isTokenWindowValid()).isFalse();
    }
  }

  // =========================================================================
  // Queue sub-class tests
  // =========================================================================

  @Nested
  class QueueTests {

    @Test
    void queue_setLocalProvider_returnsLocal() {
      AppProperties.Embedding.Queue queue = new AppProperties.Embedding.Queue();
      queue.setProvider("local");
      assertThat(queue.getProvider()).isEqualTo("local");
    }

    @Test
    void queue_setSqsProvider_returnsSqs() {
      AppProperties.Embedding.Queue queue = new AppProperties.Embedding.Queue();
      queue.setProvider("sqs");
      assertThat(queue.getProvider()).isEqualTo("sqs");
    }
  }

  // =========================================================================
  // Model sub-class tests
  // =========================================================================

  @Nested
  class ModelTests {

    @Test
    void model_setProvider_returnsSetValue() {
      AppProperties.Embedding.Model model = new AppProperties.Embedding.Model();
      model.setProvider("text-embedding-3-small");
      assertThat(model.getProvider()).isEqualTo("text-embedding-3-small");
    }
  }

  // =========================================================================
  // AppProperties integration tests
  // =========================================================================

  @Test
  void appProperties_embeddingFieldInitializedByDefault() {
    AppProperties props = new AppProperties();
    assertThat(props.getEmbedding()).isNotNull();
  }

  @Test
  void appProperties_embeddingQueueInitializedByDefault() {
    AppProperties props = new AppProperties();
    assertThat(props.getEmbedding().getQueue()).isNotNull();
  }

  @Test
  void appProperties_embeddingModelInitializedByDefault() {
    AppProperties props = new AppProperties();
    assertThat(props.getEmbedding().getModel()).isNotNull();
  }
}