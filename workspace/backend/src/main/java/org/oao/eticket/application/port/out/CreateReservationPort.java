package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.port.out.dto.CreateReservationCommand;

public interface CreateReservationPort {
  Reservation createReservation(CreateReservationCommand cmd);
}
