package org.oao.eticket.exception;

public class PreemptVacancyFailureException extends RuntimeException {
  public PreemptVacancyFailureException(final String message) {
    super(message);
  }

  public PreemptVacancyFailureException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public PreemptVacancyFailureException(final Throwable cause) {
    super(cause);
  }
}
