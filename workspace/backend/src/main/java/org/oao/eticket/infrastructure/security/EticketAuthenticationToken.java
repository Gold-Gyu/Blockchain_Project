package org.oao.eticket.infrastructure.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.io.Serial;
import java.util.Collection;

public class EticketAuthenticationToken extends AbstractAuthenticationToken {

  @Serial private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

  @Getter private final EticketAuthenticationStrategy authenticationStrategy;
  private final Object principal;
  private Object credentials;

  private EticketAuthenticationToken(
      final EticketAuthenticationStrategy authenticationStrategy,
      final Object principal,
      final Object credentials) {
    super(null);
    this.authenticationStrategy = authenticationStrategy;
    this.principal = principal;
    this.credentials = credentials;
    setAuthenticated(false);
  }

  private EticketAuthenticationToken(
      final EticketAuthenticationStrategy authenticationStrategy,
      final Object principal,
      final Object credentials,
      final Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.authenticationStrategy = authenticationStrategy;
    this.principal = principal;
    this.credentials = credentials;
    super.setAuthenticated(true);
  }

  public static EticketAuthenticationToken authenticated(
      final EticketAuthenticationStrategy authenticationStrategy,
      final Object principal,
      final Object credentials,
      final Collection<? extends GrantedAuthority> authorities) {
    return new EticketAuthenticationToken(
        authenticationStrategy, principal, credentials, authorities);
  }

  public static EticketAuthenticationToken unauthenticated(
      final EticketAuthenticationStrategy authenticationStrategy,
      final Object principal,
      final Object credentials) {
    return new EticketAuthenticationToken(authenticationStrategy, principal, credentials);
  }

  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }

  @Override
  public void setAuthenticated(final boolean isAuthenticated) {
    Assert.isTrue(!isAuthenticated, ""); // TODO(meo-s): write assertion message
    super.setAuthenticated(false);
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.credentials = null;
  }

  public UsernamePasswordAuthenticationToken migrateToUsernamePasswordAuthenticationToken() {
    final var usernamePasswordAuthenticationToken =
        isAuthenticated()
            ? UsernamePasswordAuthenticationToken.authenticated(
                principal, credentials, getAuthorities())
            : UsernamePasswordAuthenticationToken.unauthenticated(principal, credentials);
    usernamePasswordAuthenticationToken.setDetails(getDetails());
    return usernamePasswordAuthenticationToken;
  }
}
