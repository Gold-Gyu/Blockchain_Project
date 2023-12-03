package org.oao.eticket.exception;

public class ExternalServiceException extends RuntimeException {

  public ExternalServiceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ExternalServiceException(final Throwable cause) {
    super(cause);
  }
}
