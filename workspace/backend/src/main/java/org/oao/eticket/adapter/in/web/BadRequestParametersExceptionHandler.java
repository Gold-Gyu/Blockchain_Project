package org.oao.eticket.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BadRequestParametersExceptionHandler {
  private static final String MESSAGE =
      "a parameter \"%s\" does not provide, or contains illegal value.";

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ApiErrorResponse catchValidationFailure(final MethodArgumentNotValidException e) {
    return ApiErrorResponse.builder()
        .withMessage(String.format(MESSAGE, e.getFieldError().getField()))
        .build();
  }
}
