package org.oao.eticket.infrastructure.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class EticketAuthenticationConverter implements AuthenticationConverter {

  private static final String AUTHENTICATION_STRATEGY_HEADER_NAME = "X-Authentication-Strategy";

  private final Map<EticketAuthenticationStrategy, List<AuthenticationConverter>> converters;

  public EticketAuthenticationConverter(final List<ConcreteAuthenticationConverter> converters) {
    this.converters = new HashMap<>();

    for (final var converter : converters) {
      final var authenticationStrategy = converter.getSupportedAuthenticationStrategy();

      if (!this.converters.containsKey(authenticationStrategy)) {
        this.converters.put(authenticationStrategy, new ArrayList<>());
      }

      this.converters.get(authenticationStrategy).add(converter);
    }
  }

  @Override
  public Authentication convert(final HttpServletRequest request) {
    final var authenticationStrategyName = request.getHeader(AUTHENTICATION_STRATEGY_HEADER_NAME);
    if (authenticationStrategyName == null) {
      throw new BadAuthenticationRequestException(
          "Missing required header: " + AUTHENTICATION_STRATEGY_HEADER_NAME);
    }

    final var authenticationStrategy = EticketAuthenticationStrategy.of(authenticationStrategyName);
    if (authenticationStrategy == EticketAuthenticationStrategy.UNKNOWN) {
      throw new BadAuthenticationRequestException(
          "Unsupported authentication strategy: " + authenticationStrategyName);
    }

    Authentication authentication = null;
    for (final var converter : this.converters.get(authenticationStrategy)) {
      authentication = converter.convert(request);
      if (authentication != null) {
        break;
      }
    }

    return authentication;
  }
}
