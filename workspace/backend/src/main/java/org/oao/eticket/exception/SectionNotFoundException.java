package org.oao.eticket.exception;

public class SectionNotFoundException extends RuntimeException {
  public SectionNotFoundException(String message) {
    super(message);
  }

  public SectionNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public SectionNotFoundException(Throwable cause) {
    super(cause);
  }
}
