package org.oao.eticket.application.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class Venue {
  VenueId id;
  String name;
  String address;
  BigDecimal latitude;
  BigDecimal longitude;

  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class VenueId {
    private final int value;

    public static VenueId of(final int value) {
      return new VenueId(value);
    }
  }
}
