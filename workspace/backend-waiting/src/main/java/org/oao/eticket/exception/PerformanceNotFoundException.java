package org.oao.eticket.exception;

public class PerformanceNotFoundException extends RuntimeException {
    public PerformanceNotFoundException(final String message) {
        super(message);
    }

    public PerformanceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PerformanceNotFoundException(final Throwable cause) {
        super(cause);
    }

}
