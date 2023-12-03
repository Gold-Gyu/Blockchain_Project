package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.PerformanceScheduleSeatTable;

import java.util.List;

public interface SaveVacanciesToRedisUseCase {
  List<PerformanceScheduleSeatTable> saveVacanciesToRedis();
}
