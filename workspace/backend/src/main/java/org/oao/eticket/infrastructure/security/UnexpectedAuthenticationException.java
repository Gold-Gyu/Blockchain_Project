package org.oao.eticket.infrastructure.security;

import org.springframework.security.core.AuthenticationException;

class UnexpectedAuthenticationException extends AuthenticationException {
  public UnexpectedAuthenticationException(final String msg, final Throwable cause) {
    super(msg, cause);
  }

  public UnexpectedAuthenticationException(final String msg) {
    super(msg);
  }
}
