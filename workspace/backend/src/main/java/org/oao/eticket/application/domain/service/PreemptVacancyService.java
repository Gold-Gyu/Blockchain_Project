package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Seat;
import org.oao.eticket.application.port.in.PreemptVacancyUseCase;
import org.oao.eticket.application.port.out.dto.PreemptVacancyCommand;
import org.oao.eticket.application.port.out.PreemptVacancyPort;
import org.oao.eticket.common.annotation.UseCase;

@UseCase
@RequiredArgsConstructor
public class PreemptVacancyService implements PreemptVacancyUseCase {
  private final PreemptVacancyPort preemptVacancyPort;

  @Override
  public Seat preemptVacancy(org.oao.eticket.application.port.in.dto.PreemptVacancyCommand cmd) {
    final var findVacancyCommand =
        PreemptVacancyCommand.builder()
            .performanceScheduleId(cmd.getPerformanceScheduleId())
            .sectionId(cmd.getSectionId())
            .seatId(cmd.getSeatId())
            .build();
    // Entity -> Model Mapping
    return preemptVacancyPort.preemptVacancy(findVacancyCommand);
  }
}
