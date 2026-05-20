package org.eni.koinoniadaily.infrastructure.embedding.models.textembedding3small;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TextEmbedding3SmallEmbeddingModelProviderTest {

  @Mock
  private EmbeddingModel embeddingModel;

  private TextEmbedding3SmallEmbeddingModelProvider provider;

  @BeforeEach
  void setUp() {
    provider = new TextEmbedding3SmallEmbeddingModelProvider(embeddingModel);
  }

  @Test
  void getName_returnsTextEmbedding3Small() {
    assertThat(provider.getName()).isEqualTo("text-embedding-3-small");
  }

  @Test
  void getBatchSize_returns20() {
    assertThat(provider.getBatchSize()).isEqualTo(20);
  }

  @Nested
  class EmbedSingleTextTests {

    @Test
    void embed_singleText_delegatesToEmbeddingModel() {
      float[] expected = {0.1f, 0.2f, 0.3f};
      when(embeddingModel.embed("hello world")).thenReturn(expected);

      float[] result = provider.embed("hello world");

      assertThat(result).isEqualTo(expected);
      verify(embeddingModel).embed("hello world");
    }

    @Test
    void embed_emptyString_delegatesToEmbeddingModel() {
      float[] expected = new float[1536];
      when(embeddingModel.embed("")).thenReturn(expected);

      float[] result = provider.embed("");

      assertThat(result).hasSize(1536);
    }

    @Test
    void embed_singleText_returnsModelOutput() {
      float[] vector = new float[]{0.5f, -0.3f, 0.8f};
      when(embeddingModel.embed(anyString())).thenReturn(vector);

      float[] result = provider.embed("some text");

      assertThat(result).isEqualTo(vector);
    }

    @Test
    void embed_modelThrowsException_propagatesIt() {
      when(embeddingModel.embed(anyString())).thenThrow(new RuntimeException("API error"));

      assertThatThrownBy(() -> provider.embed("text"))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("API error");
    }
  }

  @Nested
  class EmbedBatchTextsTests {

    @Test
    void embed_batchTexts_delegatesToEmbeddingModelEmbedForResponse() {
      List<String> texts = List.of("text one", "text two");
      float[] emb1 = {0.1f, 0.2f};
      float[] emb2 = {0.3f, 0.4f};

      Embedding embedding1 = mock(Embedding.class);
      Embedding embedding2 = mock(Embedding.class);
      when(embedding1.getOutput()).thenReturn(emb1);
      when(embedding2.getOutput()).thenReturn(emb2);

      EmbeddingResponse response = mock(EmbeddingResponse.class);
      when(response.getResults()).thenReturn(List.of(embedding1, embedding2));
      when(embeddingModel.embedForResponse(texts)).thenReturn(response);

      List<float[]> result = provider.embed(texts);

      assertThat(result).hasSize(2);
      assertThat(result.get(0)).isEqualTo(emb1);
      assertThat(result.get(1)).isEqualTo(emb2);
    }

    @Test
    void embed_emptyList_returnsEmptyList() {
      EmbeddingResponse response = mock(EmbeddingResponse.class);
      when(response.getResults()).thenReturn(List.of());
      when(embeddingModel.embedForResponse(List.of())).thenReturn(response);

      List<float[]> result = provider.embed(List.of());

      assertThat(result).isEmpty();
    }

    @Test
    void embed_singleTextInBatch_returnsOneEmbedding() {
      List<String> texts = List.of("single text");
      float[] emb = {0.1f, 0.2f, 0.3f};

      Embedding embedding = mock(Embedding.class);
      when(embedding.getOutput()).thenReturn(emb);

      EmbeddingResponse response = mock(EmbeddingResponse.class);
      when(response.getResults()).thenReturn(List.of(embedding));
      when(embeddingModel.embedForResponse(texts)).thenReturn(response);

      List<float[]> result = provider.embed(texts);

      assertThat(result).hasSize(1);
      assertThat(result.get(0)).isEqualTo(emb);
    }

    @Test
    void embed_batchTexts_preservesOrderOfEmbeddings() {
      List<String> texts = List.of("alpha", "beta", "gamma");
      float[] emb1 = {1.0f};
      float[] emb2 = {2.0f};
      float[] emb3 = {3.0f};

      Embedding e1 = mock(Embedding.class);
      Embedding e2 = mock(Embedding.class);
      Embedding e3 = mock(Embedding.class);
      when(e1.getOutput()).thenReturn(emb1);
      when(e2.getOutput()).thenReturn(emb2);
      when(e3.getOutput()).thenReturn(emb3);

      EmbeddingResponse response = mock(EmbeddingResponse.class);
      when(response.getResults()).thenReturn(List.of(e1, e2, e3));
      when(embeddingModel.embedForResponse(texts)).thenReturn(response);

      List<float[]> result = provider.embed(texts);

      assertThat(result.get(0)).isEqualTo(emb1);
      assertThat(result.get(1)).isEqualTo(emb2);
      assertThat(result.get(2)).isEqualTo(emb3);
    }

    @Test
    void embed_modelThrowsException_propagatesIt() {
      when(embeddingModel.embedForResponse(anyList()))
          .thenThrow(new RuntimeException("Rate limit exceeded"));

      assertThatThrownBy(() -> provider.embed(List.of("text")))
          .isInstanceOf(RuntimeException.class)
          .hasMessageContaining("Rate limit exceeded");
    }
  }
}