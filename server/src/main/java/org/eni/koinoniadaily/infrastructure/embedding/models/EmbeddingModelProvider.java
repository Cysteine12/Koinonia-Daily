package org.eni.koinoniadaily.infrastructure.embedding.models;

import java.util.List;

public interface EmbeddingModelProvider {

  String getName();

  int getBatchSize();

  float[] embed(String text);

  List<float[]> embed(List<String> texts);
}
