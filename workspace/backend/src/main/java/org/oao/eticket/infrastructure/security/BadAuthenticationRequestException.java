package org.oao.eticket.infrastructure.security;

import org.springframework.security.core.AuthenticationException;

class BadAuthenticationRequestException extends AuthenticationException {

    BadAuthenticationRequestException(final String msg) {
        super(msg);
    }

    public BadAuthenticationRequestException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
