package org.oao.eticket.exception;

public class WalletDuplicateException extends RuntimeException {
  public WalletDuplicateException(String message) {
    super(message);
  }

  public WalletDuplicateException(String message, Throwable cause) {
    super(message, cause);
  }

  public WalletDuplicateException(Throwable cause) {
    super(cause);
  }
}
