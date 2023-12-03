package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.Section;

import java.util.List;

public interface GetSectionsUseCase {
    List<Section> getSections(Integer performanceScheduleId);
}
