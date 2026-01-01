package org.eni.koinoniadaily.modules.transcript;

import org.eni.koinoniadaily.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "transcripts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Transcript extends BaseEntity {

  @Column(nullable = false, columnDefinition = "text")
  private String message;
}
