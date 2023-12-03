package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.PerformanceScheduleSeatTable;

import java.util.List;

public interface LoadPerformanceScheduleSeatTablePort {
  List<PerformanceScheduleSeatTable> loadSeatTablesOpenToday();
}
