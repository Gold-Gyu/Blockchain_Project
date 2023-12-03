package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "seat")
public class SeatJpaEntity {
  @Id
  @Column(name = "seat_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "section_id")
  private SectionJpaEntity sectionJpaEntity;

  @Column(name = "seat_row")
  private String row;

  @Column(name = "number")
  private String number;

  @Builder
  public SeatJpaEntity(Integer id, SectionJpaEntity sectionJpaEntity, String row, String number) {
    this.id = id;
    this.sectionJpaEntity = sectionJpaEntity;
    this.row = row;
    this.number = number;
  }
}
