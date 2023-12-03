package org.oao.eticket.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.oao.eticket.common.validation.Validation;
import org.oao.eticket.exception.UnexpectedException;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationTokenConverter
    implements ConcreteAuthenticationConverter {

  private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource =
      new WebAuthenticationDetailsSource();

  private final ObjectMapper objectMapper;

  @Value
  private static class RequestBody {
    @NotBlank String username;
    @NotBlank String password;
  }

  private RequestBody obtainPayload(final HttpServletRequest request) {
    try {
      final var payload = objectMapper.readValue(request.getReader(), RequestBody.class);
      Validation.validate(payload);
      return payload;
    } catch (ConstraintViolationException e) {
      throw new BadCredentialsException("Bad credentials:", e);
    } catch (IOException e) {
      throw new UnexpectedException("Authentication conversion failed:", e);
    }
  }

  @Override
  public Authentication convert(final HttpServletRequest request) {
    final var payload = obtainPayload(request);
    final var authenticationToken =
        UsernamePasswordAuthenticationToken.unauthenticated(
            payload.getUsername(), payload.getPassword());

    authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
    return authenticationToken;
  }

  @Override
  public EticketAuthenticationStrategy getSupportedAuthenticationStrategy() {
    return EticketAuthenticationStrategy.BASIC;
  }
}
