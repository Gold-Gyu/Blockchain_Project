package org.oao.eticket.infrastructure.security;

import lombok.Getter;
import org.oao.eticket.common.Pair;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collection;

public class PersonalSignAuthenticationToken extends AbstractAuthenticationToken {

  private Object principal;
  @Getter private String challenge;
  @Getter private String siweMessage;
  @Getter private String personalSign;

  PersonalSignAuthenticationToken(
      final Object principal,
      final String challenge,
      final String siweMessage,
      final String personalSign) {

    super(null);
    this.principal = principal;
    this.challenge = challenge;
    this.siweMessage = siweMessage;
    this.personalSign = personalSign;
  }

  PersonalSignAuthenticationToken(
      final Object principal,
      final String challenge,
      final String siweMessage,
      final String personalSign,
      final Collection<? extends GrantedAuthority> authorities) {

    super(authorities);
    this.principal = principal;
    this.challenge = challenge;
    this.siweMessage = siweMessage;
    this.personalSign = personalSign;
    super.setAuthenticated(true);
  }

  public static PersonalSignAuthenticationToken authenticated(
      final Object principal,
      final String challenge,
      final String siweMessage,
      final String personalSign,
      final Collection<? extends GrantedAuthority> authorities) {

    return new PersonalSignAuthenticationToken(
        principal, challenge, siweMessage, personalSign, authorities);
  }

  public static PersonalSignAuthenticationToken unauthenticated(
      final Object principal,
      final String challenge,
      final String siweMessage,
      final String personalSign) {

    return new PersonalSignAuthenticationToken(principal, challenge, siweMessage, personalSign);
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }

  @Override
  public Object getCredentials() {
    return siweMessage != null && personalSign != null ? Pair.of(siweMessage, personalSign) : null;
  }

  @Override
  public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
    Assert.isTrue(
        !isAuthenticated, "This authentication token can not be dynamically changed to trusted.");
    super.setAuthenticated(false);
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.siweMessage = null;
    this.personalSign = null;
  }
}
