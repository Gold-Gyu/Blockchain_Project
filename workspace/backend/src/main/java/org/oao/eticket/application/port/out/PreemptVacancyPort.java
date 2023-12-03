package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.Seat;
import org.oao.eticket.application.port.out.dto.PreemptVacancyCommand;

public interface PreemptVacancyPort {
  Seat preemptVacancy(PreemptVacancyCommand cmd);
}
