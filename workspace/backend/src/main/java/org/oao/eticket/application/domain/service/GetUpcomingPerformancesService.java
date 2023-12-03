package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.PerformanceSummary;
import org.oao.eticket.application.port.in.GetUpcomingPerformancesUsecase;
import org.oao.eticket.application.port.out.LoadUpcomingPerformancesPort;
import org.oao.eticket.common.annotation.UseCase;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetUpcomingPerformancesService implements GetUpcomingPerformancesUsecase {
  private final LoadUpcomingPerformancesPort loadUpcomingPerformancesPort;

  @Override
  public List<PerformanceSummary> getUpcomingPerformances() {
    return loadUpcomingPerformancesPort.loadUpcomings();
  }
}
