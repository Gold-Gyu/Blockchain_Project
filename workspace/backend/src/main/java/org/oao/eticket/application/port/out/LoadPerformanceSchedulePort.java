package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.PerformanceSchedule;

public interface LoadPerformanceSchedulePort {
  PerformanceSchedule loadById(Integer id);
}
