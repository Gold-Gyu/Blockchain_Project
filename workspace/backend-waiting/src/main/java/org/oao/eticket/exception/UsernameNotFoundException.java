package org.oao.eticket.exception;

public class UsernameNotFoundException extends UserNotFoundException {

  private static final String MESSAGE = "An user whose username equals to \"%s\" not found.";

  public UsernameNotFoundException(final String username) {
    super(String.format(MESSAGE, username));
  }

  public UsernameNotFoundException(final String username, final Throwable cause) {
    super(String.format(MESSAGE, username), cause);
  }
}
