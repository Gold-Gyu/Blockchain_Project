package org.oao.eticket.application.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
public class PerformanceScheduleSeatTable {
  Integer performanceScheduleId;
  Integer sectionId;

  List<Seat> seats;

  @JsonCreator
  public PerformanceScheduleSeatTable(
      @JsonProperty("performanceScheduleId") Integer performanceScheduleId,
      @JsonProperty("sectionId") Integer sectionId,
      @JsonProperty("seats") List<Seat> seats) {
    this.performanceScheduleId = performanceScheduleId;
    this.sectionId = sectionId;
    this.seats = seats;
  }
}
