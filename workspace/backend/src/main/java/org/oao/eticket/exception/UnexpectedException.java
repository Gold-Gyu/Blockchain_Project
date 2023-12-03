package org.oao.eticket.exception;

public class UnexpectedException extends RuntimeException {

  public UnexpectedException(final String message) {
    super(message);
  }

  public UnexpectedException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public UnexpectedException(final Throwable cause) {
    super(cause);
  }
}
