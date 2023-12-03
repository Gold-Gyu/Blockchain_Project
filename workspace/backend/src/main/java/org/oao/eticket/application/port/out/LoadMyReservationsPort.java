package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.Reservation;

import java.util.List;

public interface LoadMyReservationsPort {
  List<Reservation> findMyReservations(Integer userId);
}
