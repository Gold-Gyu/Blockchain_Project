package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.PerformanceSummary;

import java.util.List;

public interface GetHotPerformancesUseCase {
  List<PerformanceSummary> getHotPerformanceList();
}
