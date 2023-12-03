package org.oao.eticket.application.port.out.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PreemptVacancyCommand {
  Integer performanceScheduleId;
  Integer sectionId;
  Integer seatId;
}
