package org.oao.eticket.application.port.in;

import org.oao.eticket.application.port.in.dto.ReserveTicketCommand;
import org.oao.eticket.application.port.in.dto.TicketReservationResult;

public interface ReserveTicketUseCase {
  TicketReservationResult reserveTicket(final ReserveTicketCommand cmd);
}
