package org.oao.eticket.application.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Reservation {
  private Integer id;
  private User user;
  private PerformanceSchedule performanceSchedule;
  private Seat seat;
  private Integer paymentAmount;
  private TicketStatus ticketStatus;
  private LocalDateTime reservationTime;
  private LocalDateTime cancellationTime;
}
