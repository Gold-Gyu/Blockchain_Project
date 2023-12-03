package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.Section;

import java.util.List;

public interface LoadPerformanceScheduleSectionsPort {
  List<Section> loadSections(Integer performanceScheduleId);
}
