package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Section;
import org.oao.eticket.application.port.in.GetSectionsUseCase;
import org.oao.eticket.application.port.out.LoadPerformanceScheduleSectionsPort;
import org.oao.eticket.application.port.out.dto.LoadSeatClassCommand;
import org.oao.eticket.application.port.out.LoadSeatClassPort;
import org.oao.eticket.common.annotation.UseCase;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetSectionsService implements GetSectionsUseCase {
  private final LoadPerformanceScheduleSectionsPort loadPerformanceScheduleSectionsPort;
  private final LoadSeatClassPort loadSeatClassPort;

  @Override
  public List<Section> getSections(Integer performanceScheduleId) {
    final var sections = loadPerformanceScheduleSectionsPort.loadSections(performanceScheduleId);
    for (Section section : sections) {
      section.setSeatClass(
          loadSeatClassPort.loadSeatClass(
              LoadSeatClassCommand.builder()
                  .sectionId(section.getId().getValue())
                  .performanceScheduleId(performanceScheduleId)
                  .build()));
    }
    return sections;
  }
}
