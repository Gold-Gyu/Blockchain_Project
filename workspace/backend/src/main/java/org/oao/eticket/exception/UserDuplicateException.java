package org.oao.eticket.exception;

public class UserDuplicateException extends RuntimeException {
  public UserDuplicateException(final Exception cause) {
    super(cause);
  }
}
