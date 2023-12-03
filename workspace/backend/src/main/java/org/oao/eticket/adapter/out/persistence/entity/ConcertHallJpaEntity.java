package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "concert_hall")
public class ConcertHallJpaEntity {
  @Id
  @Column(name = "concert_hall_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  @NotBlank
  private String name;

  @Column private String hallWholeViewImage;

  @Column(nullable = false)
  @NotBlank
  private Integer seatCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "venue_id")
  private VenueJpaEntity venueJpaEntity;

  @Builder
  public ConcertHallJpaEntity(
      Integer id,
      String name,
      String hallWholeViewImage,
      Integer seatCount,
      VenueJpaEntity venueJpaEntity) {
    this.id = id;
    this.name = name;
    this.hallWholeViewImage = hallWholeViewImage;
    this.seatCount = seatCount;
    this.venueJpaEntity = venueJpaEntity;
  }
}
