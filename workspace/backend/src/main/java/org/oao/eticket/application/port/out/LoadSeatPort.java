package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.Seat;

public interface LoadSeatPort {
  Seat loadById(Integer id);
}
