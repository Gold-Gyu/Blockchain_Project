package org.oao.eticket.application.domain.model;

import lombok.*;

import java.util.List;

@Data
@Builder
public class Section {
  SectionId id;
  String name;
  Integer sectionSeatCount;
  SeatClass seatClass;

  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class SectionId {
    private final int value;

    public static Section.SectionId of(final int value) {
      return new Section.SectionId(value);
    }
  }
}
