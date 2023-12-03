package org.oao.eticket.exception;

public class SeatClassNotFoundException extends RuntimeException {
  public SeatClassNotFoundException(String message) {
    super(message);
  }

  public SeatClassNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public SeatClassNotFoundException(Throwable cause) {
    super(cause);
  }
}
