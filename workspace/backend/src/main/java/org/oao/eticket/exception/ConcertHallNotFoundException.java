package org.oao.eticket.exception;

public class ConcertHallNotFoundException extends RuntimeException {
  public ConcertHallNotFoundException(String message) {
    super(message);
  }

  public ConcertHallNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConcertHallNotFoundException(Throwable cause) {
    super(cause);
  }
}
