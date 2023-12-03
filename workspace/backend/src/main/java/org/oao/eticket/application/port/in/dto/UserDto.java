package org.oao.eticket.application.port.in.dto;

import lombok.Value;

@Value
public class UserDto {
  int id;
  String username;
  String nickname;
  String password;
  String email;
  byte[] walletAddress;
  String role;
}
