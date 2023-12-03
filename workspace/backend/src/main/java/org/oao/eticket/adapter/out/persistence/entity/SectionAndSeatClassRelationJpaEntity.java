package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "section_and_seat_class_relation")
public class SectionAndSeatClassRelationJpaEntity {
  @Id
  @Column(name = "section_and_seat_class_relation_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "section_id")
  private SectionJpaEntity sectionJpaEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seat_class_id")
  private SeatClassJpaEntity seatClassJpaEntity;
}
