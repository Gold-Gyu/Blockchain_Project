package org.oao.eticket.application.domain.model;

import lombok.*;

@Value
public class User {

  UserID id;
  String username;
  String password;
  String nickname;
  String email;
  BlockChainWallet blockChainWallet;
  UserRole role;

  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static class UserID {
    private final int value;

    public static UserID of(final int value) {
      return new UserID(value);
    }

    public static UserID of(final String value) {
      return new UserID(Integer.parseInt(value));
    }

    @Override
    public boolean equals(final Object other) {
      return other instanceof UserID && ((UserID) other).value == value;
    }

    @Override
    public String toString() {
      return Integer.toString(value);
    }

    @Override
    public int hashCode() {
      return Integer.hashCode(value);
    }
  }
}
