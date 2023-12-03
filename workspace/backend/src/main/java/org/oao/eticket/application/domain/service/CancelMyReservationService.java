package org.oao.eticket.application.domain.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.*;
import org.oao.eticket.application.port.in.dto.CancelMyReservationCommand;
import org.oao.eticket.application.port.in.CancelMyReservationUseCase;
import org.oao.eticket.application.port.out.*;
import org.oao.eticket.common.annotation.UseCase;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
public class CancelMyReservationService implements CancelMyReservationUseCase {

  private final LoadReservationPort loadReservationPort;
  private final CancelMyReservationPort cancelMyReservationPort;

  @Transactional
  @Override
  public Reservation cancelMyReservation(final Integer id) {
    Reservation reservation = loadReservationPort.loadById(id);
    User user = reservation.getUser();
    PerformanceSchedule performanceSchedule = reservation.getPerformanceSchedule();
    Seat seat = reservation.getSeat();
    Integer paymentAmount = reservation.getPaymentAmount();
    TicketStatus ticketStatus = reservation.getTicketStatus();
    LocalDateTime reservationTime = reservation.getReservationTime();
    LocalDateTime cancellationTime = LocalDateTime.now();

    if (ticketStatus.equals(TicketStatus.SOLDOUT)) {
      Reservation cancellation =
          Reservation.builder()
              .id(id)
              .user(user)
              .performanceSchedule(performanceSchedule)
              .seat(seat)
              .paymentAmount(paymentAmount)
              .ticketStatus(TicketStatus.CANCEL)
              .reservationTime(reservationTime)
              .cancellationTime(cancellationTime)
              .build();

      return cancelMyReservationPort.cancelMyReservation(
          CancelMyReservationCommand.builder()
              .id(cancellation.getId())
              .user(cancellation.getUser())
              .performanceSchedule(cancellation.getPerformanceSchedule())
              .seat(cancellation.getSeat())
              .paymentAmount(cancellation.getPaymentAmount())
              .ticketStatus(cancellation.getTicketStatus())
              .reservationTime(cancellation.getReservationTime())
              .cancellationTime(cancellation.getCancellationTime())
              .build());
    }
    return null;
  }
}
