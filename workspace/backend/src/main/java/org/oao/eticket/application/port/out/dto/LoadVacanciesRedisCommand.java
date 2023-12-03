package org.oao.eticket.application.port.out.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoadVacanciesRedisCommand {
  Integer performanceScheduleId;
  Integer sectionId;
}
