package org.oao.eticket.application.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PerformanceSchedule {
  private Integer id;
  private LocalDateTime startDateTime;
  private Performance performance;
}
