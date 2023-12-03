package org.oao.eticket.application.domain.model;

import lombok.*;

import java.util.List;

@Data
@Builder
public class ConcertHall {
  ConcertHallId id;
  String name;
  String hallWholeViewImage;
  Integer seatCount;
  Venue venue;

  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ConcertHallId {
    private final int value;

    public static ConcertHallId of(final int value) {
      return new ConcertHallId(value);
    }
  }
}
