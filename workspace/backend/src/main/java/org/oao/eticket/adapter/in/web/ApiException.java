package org.oao.eticket.adapter.in.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
  private final HttpStatus status;

  private ApiException(final HttpStatus status, final String message, final Exception cause) {
    super(message, cause);
    this.status = status;
  }

  private ApiException(final HttpStatus status, final String message) {
    super(message);
    this.status = status;
  }

  public static ApiExceptionBuilder builder() {
    return new ApiExceptionBuilder();
  }

  public static class ApiExceptionBuilder {

    private Exception cause;
    private HttpStatus status;
    private String message;

    private ApiExceptionBuilder() {}

    public ApiExceptionBuilder withStatus(final HttpStatus status) {
      this.status = status;
      return this;
    }

    public ApiExceptionBuilder withMessage(final String message) {
      this.message = message;
      return this;
    }

    public ApiExceptionBuilder withCause(final Exception e) {
      this.cause = e;
      return this;
    }

    public ApiException build() {
      return cause != null
          ? new ApiException(status, message, cause)
          : new ApiException(status, message);
    }
  }
}
