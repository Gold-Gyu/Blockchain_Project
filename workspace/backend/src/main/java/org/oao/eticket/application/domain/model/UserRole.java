package org.oao.eticket.application.domain.model;

import lombok.Getter;

@Getter
public enum UserRole {
  GUEST("GUEST"),
  HOST("HOST");

  private final String roleName;

  UserRole(final String roleName) {
    this.roleName = roleName;
  }

  public static UserRole of(final String roleName) {
    switch (roleName) {
      case "HOST":
        return HOST;
      default:
        return GUEST;
    }
  }

  @Override
  public String toString() {
    return roleName;
  }
}
