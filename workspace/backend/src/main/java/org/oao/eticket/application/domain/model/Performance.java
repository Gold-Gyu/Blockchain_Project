package org.oao.eticket.application.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Performance {
  PerformanceId id;
  String title;
  PerformanceGenre genre;
  String cast;
  String description;
  String posterImagePath;
  String detailImagePath;
  Integer runningTime;
  LocalDateTime ticketingOpenDateTime;
  ConcertHall concertHall;
  List<SeatClass> seatClassList;
  User host;
  List<PerformanceSchedule> performanceScheduleList;

  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class PerformanceId {
    private final int value;

    public static PerformanceId of(final int value) {
      return new PerformanceId(value);
    }
  }
}
