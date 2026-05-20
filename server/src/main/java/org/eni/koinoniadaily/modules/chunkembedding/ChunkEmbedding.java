package org.eni.koinoniadaily.modules.chunkembedding;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.modules.teachingchunk.TeachingChunk;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "chunk_embeddings", uniqueConstraints = {
    @UniqueConstraint(
        name = "teaching_chunk_id_model_unique",
        columnNames = {"teaching_chunk_id", "model"}
    )
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ChunkEmbedding extends BaseEntity {

  @JoinColumn(name = "teaching_chunk_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private TeachingChunk teachingChunk;

  @Column(nullable = false, length = 100)
  private String model;

  @Column(nullable = false, columnDefinition = "vector(1536)")
  @JdbcTypeCode(SqlTypes.VECTOR)
  private float[] embedding;
}
