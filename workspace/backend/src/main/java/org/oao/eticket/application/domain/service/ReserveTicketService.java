package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.*;
import org.oao.eticket.application.port.in.dto.ReserveTicketCommand;
import org.oao.eticket.application.port.in.ReserveTicketUseCase;
import org.oao.eticket.application.port.in.dto.TicketReservationResult;
import org.oao.eticket.application.port.out.*;
import org.oao.eticket.application.port.out.dto.CreateReservationCommand;
import org.oao.eticket.common.annotation.UseCase;
import org.oao.eticket.exception.ExternalServiceException;
import org.oao.eticket.exception.ServiceFailureException;
import org.oao.eticket.exception.UnexpectedException;
import org.oao.eticket.exception.UserNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class ReserveTicketService implements ReserveTicketUseCase {

  private final CreateReservationPort createReservationPort;

  @Override
  @Transactional
  public TicketReservationResult reserveTicket(final ReserveTicketCommand cmd)
      throws IllegalStateException {

    final var createReservationCommand =
        CreateReservationCommand.builder()
            .buyerId(cmd.getUserId())
            .performanceScheduleId(cmd.getPerformanceScheduleId())
            .seatId(cmd.getSeatId())
            .paymentAmount(cmd.getPaymentAmount())
            .ticketStatus(TicketStatus.SOLDOUT)
            .reservationTime(LocalDateTime.now())
            .build();

    try {
      final var newReservation = createReservationPort.createReservation(createReservationCommand);
      return new TicketReservationResult(newReservation.getId());
    } catch (ExternalServiceException e) {
      throw e;
    } catch (Exception e) {
      throw new ServiceFailureException("Failed to create reservation of ticket.", e);
    }
  }
}
