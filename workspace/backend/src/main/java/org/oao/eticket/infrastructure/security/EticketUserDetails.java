package org.oao.eticket.infrastructure.security;

import org.oao.eticket.application.domain.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class EticketUserDetails implements UserDetails {

  private final User wrappedUser;
  private final List<EticketGrantedAuthority> authorities;

  private final boolean isAccountEnabled = true;
  private final boolean isAccountExpired = false;
  private final boolean isAccountLocked = false;
  private final boolean isCredentialsExpired = false;

  private EticketUserDetails(final User user) {
    this.wrappedUser = user;
    this.authorities = List.of(EticketGrantedAuthority.wrap(user.getRole()));
  }

  public static EticketUserDetails wrap(final User user) {
    return new EticketUserDetails(user);
  }

  public User unwrap() {
    return wrappedUser;
  }

  public User.UserID getId() {
    return wrappedUser.getId();
  }

  @Override
  public String getUsername() {
    return wrappedUser.getUsername() != null
        ? wrappedUser.getUsername()
        : "id:" + wrappedUser.getId();
  }

  @Override
  public String getPassword() {
    return wrappedUser.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return !isAccountExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !isAccountLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !isCredentialsExpired;
  }

  @Override
  public boolean isEnabled() {
    return isAccountEnabled;
  }

  public static Optional<EticketUserDetails> from(final Authentication authentication) {
    return authentication.getPrincipal() instanceof EticketUserDetails userDetails
        ? Optional.of(userDetails)
        : Optional.empty();
  }
}
