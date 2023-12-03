package org.oao.eticket.application.domain.model;

import lombok.Getter;

@Getter
public enum PerformanceGenre {
  MUSICAL("MUSICAL"),
  PLAY("PLAY"),
  CONCERT("CONCERT"),
  DANCE("DANCE"),
  CLASSICANDOPERA("CLASSIC & OPERA"),
  MAGICANDSHOW("MAGIC & SHOW"),
  ETC("etc");

  private final String genre;

  PerformanceGenre(final String genre) {
    this.genre = genre;
  }

  public static PerformanceGenre of(final String genre) {
    switch (genre) {
      case "MUSICAL":
        return MUSICAL;
      case "PLAY":
        return PLAY;
      case "CONCERT":
        return CONCERT;
      case "DANCE":
        return DANCE;
      case "CLASSIC & OPERA":
        return CLASSICANDOPERA;
      case "MAGIC & SHOW":
        return MAGICANDSHOW;
      default:
        return ETC;
    }
  }
}
