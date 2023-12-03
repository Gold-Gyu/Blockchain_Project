package org.oao.eticket.exception;

public class SeatTableDuplicatedException extends RuntimeException {
  public SeatTableDuplicatedException(String message) {
    super(message);
  }

  public SeatTableDuplicatedException(final Exception cause) {
    super(cause);
  }
}
