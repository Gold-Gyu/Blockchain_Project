package org.oao.eticket.application.port.in.dto;

import lombok.Builder;
import lombok.Value;
import org.oao.eticket.application.domain.model.Vacancy;

import java.util.List;

@Value
@Builder
public class SaveSeatsToRedisCommand {
  Integer performanceScheduleId;
  List<Vacancy> vacancyList;
}
