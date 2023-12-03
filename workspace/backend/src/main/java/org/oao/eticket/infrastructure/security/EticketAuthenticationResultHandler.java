package org.oao.eticket.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.AccessTokenMetadata;
import org.oao.eticket.application.domain.model.RefreshTokenMetadata;
import org.oao.eticket.application.port.in.CreateAuthTokenUseCase;
import org.oao.eticket.common.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class EticketAuthenticationResultHandler
    implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

  private static final String MESSAGE_FOR_SUCCESS =
      "{\"accessToken\":\"%s\",\"refreshToken\":\"%s\"}";
  private static final String MESSAGE_FOR_FAILURE =
      "{\"summary\":\"authentication failed\",\"description\":\"%s\"}";
  private static final String MESSAGE_FOR_ERROR =
      "{\"summary\":\"internal server error\",\"description\":\"Unexpected error occurred.\"}";

  private final CreateAuthTokenUseCase createAuthTokenUseCase;

  @Override
  public void onAuthenticationSuccess(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final FilterChain chain,
      final Authentication authentication)
      throws IOException, ServletException {

    if (!(authentication.getPrincipal() instanceof EticketUserDetails)) {
      chain.doFilter(request, response);
      return;
    }

    onAuthenticationSuccess(request, response, authentication);
  }

  @Override
  public void onAuthenticationSuccess(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Authentication authentication)
      throws IOException {

    if (!(authentication.getPrincipal() instanceof EticketUserDetails)) {
      throw new IllegalArgumentException(); // TODO(meo-s): write error message
    }

    Pair<Pair<AccessTokenMetadata, String>, Pair<RefreshTokenMetadata, String>> tokenPair;
    try {
      final var userDetails = ((EticketUserDetails) authentication.getPrincipal());
      tokenPair = createAuthTokenUseCase.create(userDetails.unwrap());
    } catch (Exception e) {
      e.printStackTrace(); // TODO(meo-s): use logging library

      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setContentType("application/json; charset=utf-8");
      response.getWriter().write(MESSAGE_FOR_ERROR);
      return;
    }

    response.setStatus(HttpStatus.OK.value());
    response.setContentType("application/json; charset=utf-8");
    response.getWriter().write(String.format(MESSAGE_FOR_SUCCESS, tokenPair.x.y, tokenPair.y.y));
  }

  @Override
  public void onAuthenticationFailure(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final AuthenticationException exception)
      throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setCharacterEncoding("utf-8");
    response.setContentType("application/json; charset=utf-8");
    response.getWriter().write(String.format(MESSAGE_FOR_FAILURE, exception.getMessage()));
  }
}
