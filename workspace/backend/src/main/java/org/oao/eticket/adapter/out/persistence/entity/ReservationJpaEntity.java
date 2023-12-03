package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.oao.eticket.application.domain.model.TicketStatus;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reservation")
public class ReservationJpaEntity {

  @Id
  @Column(name = "reservation_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserJpaEntity userJpaEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "performance_schedule_id")
  private PerformanceScheduleJpaEntity performanceScheduleJpaEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seat_id")
  private SeatJpaEntity seatJpaEntity;

  @Column(name = "payment_amount")
  private Integer paymentAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TicketStatus ticketStatus;

  @Column(name = "reservation_time")
  private LocalDateTime reservationTime;

  @Column(name = "cancellation_time")
  private LocalDateTime cancellationTime;

  @Builder
  public ReservationJpaEntity(
      Integer id,
      UserJpaEntity userJpaEntity,
      PerformanceScheduleJpaEntity performanceScheduleJpaEntity,
      SeatJpaEntity seatJpaEntity,
      Integer paymentAmount,
      TicketStatus ticketStatus,
      LocalDateTime reservationTime,
      LocalDateTime cancellationTime) {
    this.id = id;
    this.userJpaEntity = userJpaEntity;
    this.performanceScheduleJpaEntity = performanceScheduleJpaEntity;
    this.seatJpaEntity = seatJpaEntity;
    this.paymentAmount = paymentAmount;
    this.ticketStatus = ticketStatus;
    this.reservationTime = reservationTime;
    this.cancellationTime = cancellationTime;
  }
}
