package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.Performance;

public interface LoadPerformanceDetailPort {
  Performance loadById(Performance.PerformanceId performanceId);
}
