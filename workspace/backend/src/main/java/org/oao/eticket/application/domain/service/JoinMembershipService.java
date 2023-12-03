package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.BlockChainWallet;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.domain.model.UserRole;
import org.oao.eticket.application.port.in.dto.JoinMembershipCommand;
import org.oao.eticket.application.port.in.JoinMembershipUseCase;
import org.oao.eticket.application.port.out.dto.CreateUserCommand;
import org.oao.eticket.application.port.out.CreateUserPort;
import org.oao.eticket.common.annotation.UseCase;

@UseCase
@RequiredArgsConstructor
class JoinMembershipService implements JoinMembershipUseCase {
  private final CreateUserPort createUserPort;

  @Override
  public User join(final JoinMembershipCommand cmd) {
    final var createUserCommand =
        new CreateUserCommand(
            cmd.getUsername(),
            cmd.getPassword(),
            cmd.getEmail(),
            cmd.getNickname(),
            BlockChainWallet.NULL_WALLET.getAddress(),
            UserRole.GUEST);
    return createUserPort.create(createUserCommand);
  }
}
