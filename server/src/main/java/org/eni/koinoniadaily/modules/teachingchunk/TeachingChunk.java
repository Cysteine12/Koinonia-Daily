package org.eni.koinoniadaily.modules.teachingchunk;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.modules.chunkembedding.ChunkEmbedding;
import org.eni.koinoniadaily.modules.teaching.Teaching;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teaching_chunks", uniqueConstraints = {
    @UniqueConstraint(name = "teaching_id_chunk_index_unique", columnNames = {"teaching_id", "chunk_index"})
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class TeachingChunk extends BaseEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teaching_id", nullable = false)
  private Teaching teaching;

  @Column(nullable = false)
  private int chunkIndex;

  @Column(nullable = false, columnDefinition = "text")
  private String content;

  @Column(nullable = false, length = 60)
  private String teachingTitle;

  @Column(nullable = true, length = 100)
  private String sectionTitle;

  @Column(nullable = true)
  private Integer startOffset;

  @Column(nullable = false, length = 20)
  @Enumerated(value = EnumType.STRING)
  @Builder.Default
  private EmbeddingStatus embeddingStatus = EmbeddingStatus.PENDING;

  @OneToMany(mappedBy = "teachingChunk", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<ChunkEmbedding> chunkEmbeddings = new ArrayList<>();
}
