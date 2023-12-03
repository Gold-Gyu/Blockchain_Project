package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.Seat;
import org.oao.eticket.application.domain.model.Vacancy;
import org.oao.eticket.application.port.in.dto.GetVacanciesCommand;

import java.util.List;

public interface GetVacanciesUseCase {
  List<Seat> getVacancies(GetVacanciesCommand getVacanciesCommand);
}
