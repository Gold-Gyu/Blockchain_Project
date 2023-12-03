package org.oao.eticket.adapter.in.web;

import lombok.extern.slf4j.Slf4j;
import org.oao.eticket.exception.ServiceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ServiceFailureExceptionHandler {

  @ExceptionHandler(ServiceFailureException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ApiErrorResponse handleException(final ServiceFailureException e) {
    log.info("ServiceFailureException raised.", e);
    return ApiErrorResponse.builder().withMessage(e.getMessage()).build();
  }
}
