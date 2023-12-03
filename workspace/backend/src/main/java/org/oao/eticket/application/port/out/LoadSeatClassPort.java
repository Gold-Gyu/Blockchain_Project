package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.SeatClass;
import org.oao.eticket.application.port.out.dto.LoadSeatClassCommand;

public interface LoadSeatClassPort {
  SeatClass loadSeatClass(LoadSeatClassCommand loadSeatClassCommand);
}
