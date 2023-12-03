package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.Seat;
import org.oao.eticket.application.port.in.dto.PreemptVacancyCommand;

public interface PreemptVacancyUseCase {
  Seat preemptVacancy(final PreemptVacancyCommand cmd);
}
