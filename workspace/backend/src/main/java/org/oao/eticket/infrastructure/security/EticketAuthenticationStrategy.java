package org.oao.eticket.infrastructure.security;

public enum EticketAuthenticationStrategy {
  UNKNOWN(),
  BASIC(),
  PERSONAL_SIGN();

  EticketAuthenticationStrategy() {}

  public static EticketAuthenticationStrategy of(final String strategyName) {
    return switch (strategyName.toLowerCase()) {
      case "basic" -> BASIC;
      case "personal-sign" -> PERSONAL_SIGN;
      default -> UNKNOWN;
    };
  }
}
