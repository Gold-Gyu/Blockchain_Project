package org.oao.eticket.application.port.in.dto;

import lombok.Value;

@Value
public class JoinMembershipCommand {
  String username;
  String password;
  String email;
  String nickname;
}
