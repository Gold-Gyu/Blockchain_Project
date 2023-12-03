package org.oao.eticket.application.port.out.dto;

import lombok.Value;
import org.oao.eticket.application.domain.model.UserRole;

@Value
public class CreateUserCommand {
  String username;
  String password;
  String email;
  String nickname;
  byte[] walletAddress;
  UserRole role;
}
