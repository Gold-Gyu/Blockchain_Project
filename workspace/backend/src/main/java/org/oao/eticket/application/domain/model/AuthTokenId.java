package org.oao.eticket.application.domain.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthTokenId {

  private final UUID value;

  public static AuthTokenId of(final UUID value) {
    return new AuthTokenId(value);
  }

  public static AuthTokenId of(final String value) {
    return new AuthTokenId(UUID.fromString(value));
  }

  @Override
  public boolean equals(final Object other) {
    return other instanceof AuthTokenId && ((AuthTokenId) other).value.equals(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
