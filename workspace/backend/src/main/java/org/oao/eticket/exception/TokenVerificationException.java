package org.oao.eticket.exception;

public class TokenVerificationException extends Exception {

  public TokenVerificationException(final String message) {
    super(message);
  }

  public TokenVerificationException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
