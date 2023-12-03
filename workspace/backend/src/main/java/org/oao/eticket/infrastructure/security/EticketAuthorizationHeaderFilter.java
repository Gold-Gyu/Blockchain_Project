package org.oao.eticket.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.oao.eticket.application.domain.model.BlockChainWallet;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.domain.model.UserRole;
import org.oao.eticket.application.port.in.VerifyAccessTokenUseCase;
import org.oao.eticket.common.Pair;
import org.oao.eticket.exception.TokenVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class EticketAuthorizationHeaderFilter extends OncePerRequestFilter {

  private static final String TOKEN_PREFIX = "Bearer ";
  private final VerifyAccessTokenUseCase verifyAccessTokenUseCase;

  private boolean containsToken(final HttpServletRequest request) {
    final var authorizationHeader = request.getHeader("Authorization");
    return authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX);
  }

  private String obtainTokenFromRequest(final HttpServletRequest request) {
    final var authorizationHeader = request.getHeader("Authorization");
    Assert.isTrue(authorizationHeader != null, ""); // TODO(meo-s): write assertion message
    return authorizationHeader.substring(TOKEN_PREFIX.length());
  }

  @Override
  protected void doFilterInternal(
      @NotNull final HttpServletRequest request,
      @NotNull final HttpServletResponse response,
      @NotNull final FilterChain filterChain)
      throws ServletException, IOException {

    if (containsToken(request)) {
      final var token = obtainTokenFromRequest(request);

      final Pair<User.UserID, List<UserRole>> tokenPayload;
      try {
        tokenPayload = verifyAccessTokenUseCase.verify(token);
      } catch (TokenVerificationException e) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=utf-8");
        return;
      }

      final var sparseUser =
          new User(
              tokenPayload.x,
              null,
              null,
              null,
              null,
              BlockChainWallet.NULL_WALLET,
              tokenPayload.y.get(0));

      final var authentication =
          UsernamePasswordAuthenticationToken.authenticated(
              EticketUserDetails.wrap(sparseUser),
              null,
              tokenPayload.y.stream().map(EticketGrantedAuthority::wrap).toList());

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
}
