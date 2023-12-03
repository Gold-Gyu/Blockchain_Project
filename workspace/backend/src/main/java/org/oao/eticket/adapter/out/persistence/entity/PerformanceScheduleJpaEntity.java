package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "performance_schedule")
public class PerformanceScheduleJpaEntity {
  @Id
  @Column(name = "performance_schedule_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column @NotBlank private LocalDateTime startDateTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "performance_id")
  private PerformanceJpaEntity performanceJpaEntity;

  @Builder
  public PerformanceScheduleJpaEntity(
      Integer id, LocalDateTime startDateTime, PerformanceJpaEntity performanceJpaEntity) {
    this.id = id;
    this.startDateTime = startDateTime;
    this.performanceJpaEntity = performanceJpaEntity;
  }
}
