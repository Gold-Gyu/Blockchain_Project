package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.PerformanceSummary;

import java.util.List;

public interface LoadUpcomingPerformancesPort {
  List<PerformanceSummary> loadUpcomings();
}
