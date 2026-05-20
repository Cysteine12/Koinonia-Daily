package org.eni.koinoniadaily.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the new Embedding configuration nested classes added in AppProperties.
 * Tests focus on validation logic (isTokenWindowValid) and default values.
 */
class AppPropertiesEmbeddingTest {

  // -----------------------------------------------------------------------
  // AppProperties.Embedding - default values
  // -----------------------------------------------------------------------

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
  void embedding_defaultValues_tokenWindowIsValid() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();

    assertThat(embedding.isTokenWindowValid()).isTrue();
  }

  // -----------------------------------------------------------------------
  // AppProperties.Embedding.isTokenWindowValid()
  // -----------------------------------------------------------------------

  @Test
  void isTokenWindowValid_overlapLessThanMax_returnsTrue() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setMaxChunkToken(1200);
    embedding.setOverlapToken(150);

    assertThat(embedding.isTokenWindowValid()).isTrue();
  }

  @Test
  void isTokenWindowValid_overlapEqualToMax_returnsFalse() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setMaxChunkToken(100);
    embedding.setOverlapToken(100);

    assertThat(embedding.isTokenWindowValid()).isFalse();
  }

  @Test
  void isTokenWindowValid_overlapGreaterThanMax_returnsFalse() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setMaxChunkToken(100);
    embedding.setOverlapToken(200);

    assertThat(embedding.isTokenWindowValid()).isFalse();
  }

  @Test
  void isTokenWindowValid_overlapIsZero_returnsTrue() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setMaxChunkToken(100);
    embedding.setOverlapToken(0);

    assertThat(embedding.isTokenWindowValid()).isTrue();
  }

  @Test
  void isTokenWindowValid_overlapIsOneLesstThanMax_returnsTrue() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setMaxChunkToken(100);
    embedding.setOverlapToken(99);

    assertThat(embedding.isTokenWindowValid()).isTrue();
  }

  @Test
  void isTokenWindowValid_overlapIsOneMoreThanMax_returnsFalse() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setMaxChunkToken(100);
    embedding.setOverlapToken(101);

    assertThat(embedding.isTokenWindowValid()).isFalse();
  }

  @Test
  void isTokenWindowValid_maxTokenIsOne_overlapIsZero_returnsTrue() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setMaxChunkToken(1);
    embedding.setOverlapToken(0);

    assertThat(embedding.isTokenWindowValid()).isTrue();
  }

  // -----------------------------------------------------------------------
  // AppProperties.Embedding.Queue - nested class
  // -----------------------------------------------------------------------

  @Test
  void embeddingQueue_setProvider_returnsCorrectValue() {
    AppProperties.Embedding.Queue queue = new AppProperties.Embedding.Queue();
    queue.setProvider("local");

    assertThat(queue.getProvider()).isEqualTo("local");
  }

  @Test
  void embeddingQueue_setProviderSqs_returnsCorrectValue() {
    AppProperties.Embedding.Queue queue = new AppProperties.Embedding.Queue();
    queue.setProvider("sqs");

    assertThat(queue.getProvider()).isEqualTo("sqs");
  }

  // -----------------------------------------------------------------------
  // AppProperties.Embedding.Model - nested class
  // -----------------------------------------------------------------------

  @Test
  void embeddingModel_setProvider_returnsCorrectValue() {
    AppProperties.Embedding.Model model = new AppProperties.Embedding.Model();
    model.setProvider("text-embedding-3-small");

    assertThat(model.getProvider()).isEqualTo("text-embedding-3-small");
  }

  // -----------------------------------------------------------------------
  // AppProperties.Embedding - setter/getter roundtrip
  // -----------------------------------------------------------------------

  @Test
  void embedding_setQueue_returnsCorrectQueue() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    AppProperties.Embedding.Queue queue = new AppProperties.Embedding.Queue();
    queue.setProvider("local");
    embedding.setQueue(queue);

    assertThat(embedding.getQueue().getProvider()).isEqualTo("local");
  }

  @Test
  void embedding_setModel_returnsCorrectModel() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    AppProperties.Embedding.Model model = new AppProperties.Embedding.Model();
    model.setProvider("text-embedding-3-small");
    embedding.setModel(model);

    assertThat(embedding.getModel().getProvider()).isEqualTo("text-embedding-3-small");
  }

  @Test
  void embedding_setMaxChunkToken_updatesValue() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setMaxChunkToken(500);

    assertThat(embedding.getMaxChunkToken()).isEqualTo(500);
  }

  @Test
  void embedding_setOverlapToken_updatesValue() {
    AppProperties.Embedding embedding = new AppProperties.Embedding();
    embedding.setOverlapToken(50);

    assertThat(embedding.getOverlapToken()).isEqualTo(50);
  }
}