package org.oao.eticket.application.domain.model;

public enum SeatStatus {
  ONSALE("ONSALE"),
  PREEMPTED("PREEMPTED"),
  SALED("SALED");

  private final String status;

  SeatStatus(final String status) {
    this.status = status;
  }

  public static SeatStatus of(final String status) {
    switch (status) {
      case "PREEMPTED":
        return PREEMPTED;
      case "SALED":
        return SALED;
      default:
        return ONSALE;
    }
  }

  @Override
  public String toString() {
    return status;
  }
}
