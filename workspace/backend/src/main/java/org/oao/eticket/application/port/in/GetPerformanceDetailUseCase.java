package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.Performance;

public interface GetPerformanceDetailUseCase {
  Performance getPerformance(Integer performanceId);
}
