package org.oao.eticket.exception;

public class ServiceFailureException extends RuntimeException {
  public ServiceFailureException() {
    super();
  }

  public ServiceFailureException(final String message) {
    super(message);
  }

  public ServiceFailureException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ServiceFailureException(final Throwable cause) {
    super(cause);
  }
}
