package org.oao.eticket.application.port.in.dto;

import lombok.Builder;
import lombok.Value;
import org.oao.eticket.application.domain.model.User;

@Value
@Builder
public class ReserveTicketCommand {
  User.UserID userId;
  Integer performanceScheduleId;
  Integer seatId;
  Integer paymentAmount;
}
