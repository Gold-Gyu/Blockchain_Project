package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Performance;
import org.oao.eticket.application.port.in.GetPerformanceDetailUseCase;
import org.oao.eticket.application.port.out.LoadPerformanceDetailPort;
import org.oao.eticket.common.annotation.UseCase;

@UseCase
@RequiredArgsConstructor
public class GetPerformanceDetailService implements GetPerformanceDetailUseCase {
  private final LoadPerformanceDetailPort loadPerformanceDetailPort;

  @Override
  public Performance getPerformance(Integer performanceId) {
    return loadPerformanceDetailPort.loadById(Performance.PerformanceId.of(performanceId));
  }
}
