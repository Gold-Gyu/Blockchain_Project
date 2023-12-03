package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "venue")
public class VenueJpaEntity {
  @Id
  @Column(name = "venue_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  @NotBlank
  private String name;

  @Column(nullable = false)
  @NotBlank
  private String address;

  @Column(nullable = false)
  @NotBlank
  private BigDecimal latitude;

  @Column(nullable = false)
  @NotBlank
  private BigDecimal longitude;

  @Builder
  public VenueJpaEntity(
      Integer id, String name, String address, BigDecimal latitude, BigDecimal longitude) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
