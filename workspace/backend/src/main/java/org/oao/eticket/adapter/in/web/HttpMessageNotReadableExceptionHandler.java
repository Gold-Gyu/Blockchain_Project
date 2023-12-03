package org.oao.eticket.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HttpMessageNotReadableExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ApiErrorResponse handleException(final HttpMessageNotReadableException e) {
    return ApiErrorResponse.builder().withMessage(e.getMessage()).build();
  }
}
