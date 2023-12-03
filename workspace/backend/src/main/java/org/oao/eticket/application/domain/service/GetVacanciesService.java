package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Seat;
import org.oao.eticket.application.domain.model.Vacancy;
import org.oao.eticket.application.port.in.dto.GetVacanciesCommand;
import org.oao.eticket.application.port.in.GetVacanciesUseCase;
import org.oao.eticket.application.port.out.dto.LoadVacanciesRedisCommand;
import org.oao.eticket.application.port.out.LoadVacanciesRedisPort;
import org.oao.eticket.common.annotation.UseCase;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetVacanciesService implements GetVacanciesUseCase {
  private final LoadVacanciesRedisPort loadVacanciesRedisPort;

  @Override
  public List<Seat> getVacancies(final GetVacanciesCommand cmd) {
    final var loadVacanciesFromRedisCommand =
        LoadVacanciesRedisCommand.builder()
            .performanceScheduleId(cmd.getPerformanceScheduleId())
            .sectionId(cmd.getSectionId())
            .build();
    return loadVacanciesRedisPort.getSectionSeats(loadVacanciesFromRedisCommand);
  }
}
