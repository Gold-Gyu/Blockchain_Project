package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.Reservation;

import java.util.List;

public interface LoadMyTicketsUseCase {
  List<Reservation> loadMyTickets(Integer userId);
}
