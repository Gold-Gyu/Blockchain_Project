package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.oao.eticket.application.domain.model.SeatClass;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "seat_class")
public class SeatClassJpaEntity {
  @Id
  @Column(name = "seat_class_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "performance_id")
  private PerformanceJpaEntity performanceJpaEntity;

  @Column(nullable = false)
  @NotBlank
  private String className;

  @Column(nullable = false)
  @NotBlank
  private Integer price;

  @Builder
  public SeatClassJpaEntity(
      Integer id, String className, Integer price, PerformanceJpaEntity performanceJpaEntity) {
    this.id = id;
    this.className = className;
    this.price = price;
    this.performanceJpaEntity = performanceJpaEntity;
  }
}
