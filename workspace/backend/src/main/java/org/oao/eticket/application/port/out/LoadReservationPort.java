package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.Reservation;

public interface LoadReservationPort {
  Reservation loadById(Integer id);
}
