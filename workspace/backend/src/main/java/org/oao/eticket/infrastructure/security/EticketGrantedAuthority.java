package org.oao.eticket.infrastructure.security;

import org.oao.eticket.application.domain.model.UserRole;
import org.springframework.security.core.GrantedAuthority;

/** An adapter class for UserRole that implements GrantedAuthority interface of Spring Security. */
class EticketGrantedAuthority implements GrantedAuthority {

  private final UserRole userRole;

  private EticketGrantedAuthority(final UserRole userRole) {
    this.userRole = userRole;
  }

  static EticketGrantedAuthority wrap(final UserRole userRole) {
    return new EticketGrantedAuthority(userRole);
  }

  UserRole unwrap() {
    return userRole;
  }

  @Override
  public String getAuthority() {
    return userRole.getRoleName();
  }

  @Override
  public String toString() {
    return userRole.toString();
  }

  @Override
  public boolean equals(final Object other) {
    return other instanceof EticketGrantedAuthority && ((EticketGrantedAuthority) other).userRole == userRole;
  }

  @Override
  public int hashCode() {
    return userRole.getRoleName().hashCode();
  }
}
