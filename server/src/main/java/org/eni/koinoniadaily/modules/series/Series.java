package org.eni.koinoniadaily.modules.series;

import java.util.ArrayList;
import java.util.List;

import org.eni.koinoniadaily.entity.BaseEntity;
import org.eni.koinoniadaily.modules.teaching.Teaching;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "series")
@Getter
@Setter
@SuperBuilder 
@NoArgsConstructor
public class Series extends BaseEntity {

  @Column(nullable= false)
  private String title;

  @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = false)
  @Builder.Default
  private List<Teaching> teachings = new ArrayList<>();
}
