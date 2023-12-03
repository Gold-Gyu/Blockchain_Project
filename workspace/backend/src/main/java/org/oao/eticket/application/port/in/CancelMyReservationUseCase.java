package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.Reservation;

public interface CancelMyReservationUseCase {
  Reservation cancelMyReservation(Integer id);
}
