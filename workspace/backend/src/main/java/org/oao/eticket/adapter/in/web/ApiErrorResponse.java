package org.oao.eticket.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse {

  private String message;

  public static ApiErrorResponseBuilder builder() {
    return new ApiErrorResponseBuilder();
  }

  public static class ApiErrorResponseBuilder {
    private String message = null;

    ApiErrorResponseBuilder() {}

    public ApiErrorResponseBuilder withMessage(final String message) {
      this.message = message;
      return this;
    }

    public ApiErrorResponse build() {
      return new ApiErrorResponse(message);
    }
  }
}
