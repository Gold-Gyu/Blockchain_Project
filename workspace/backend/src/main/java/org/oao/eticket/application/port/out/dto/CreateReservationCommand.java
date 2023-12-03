package org.oao.eticket.application.port.out.dto;

import lombok.Builder;
import lombok.Value;
import org.oao.eticket.application.domain.model.*;

import java.time.LocalDateTime;

@Value
@Builder
public class CreateReservationCommand {
  User.UserID buyerId;
  int performanceScheduleId;
  int seatId;
  int paymentAmount;
  TicketStatus ticketStatus;
  LocalDateTime reservationTime;
}
