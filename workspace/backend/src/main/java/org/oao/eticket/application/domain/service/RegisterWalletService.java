package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.BlockChainWallet;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.in.RegisterWalletUseCase;
import org.oao.eticket.application.port.in.dto.RegisterWalletCommand;
import org.oao.eticket.application.port.in.dto.UserDto;
import org.oao.eticket.application.port.in.mapper.UserMapper;
import org.oao.eticket.application.port.out.LoadUserPort;
import org.oao.eticket.application.port.out.UpdateUserWalletAddressPort;
import org.oao.eticket.application.port.out.dto.UpdateUserWalletAddressCommand;
import org.oao.eticket.common.EtherUtils;
import org.oao.eticket.common.annotation.UseCase;
import org.oao.eticket.exception.ServiceFailureException;
import org.oao.eticket.exception.UnexpectedException;
import org.oao.eticket.exception.UserNotFoundException;
import org.oao.eticket.exception.WalletDuplicateException;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;

@UseCase
@RequiredArgsConstructor
public class RegisterWalletService implements RegisterWalletUseCase {

  private static final String MESSAGE_TEMPLATE =
      "I agree to register blockchain account \"%s\" to Eticket account \"%s\".";

  private final UserMapper userMapper;
  private final LoadUserPort loadUserPort;
  private final UpdateUserWalletAddressPort updateUserWalletAddressPort;

  @Override
  public UserDto register(final RegisterWalletCommand cmd) {
    final User user;
    try {
      user = loadUserPort.loadById(cmd.getUserId());
    } catch (UserNotFoundException e) {
      throw new UnexpectedException(e);
    }

    final String message =
        String.format(MESSAGE_TEMPLATE, cmd.getWalletAddress(), user.getUsername());
    final String messageInHex = Numeric.toHexString(message.getBytes(StandardCharsets.UTF_8));

    final String recoveredAddress =
        EtherUtils.recoverAddressFromPersonalSign(cmd.getPersonalSign(), messageInHex)
            .orElseGet(() -> "");
    if (!recoveredAddress.equals(cmd.getWalletAddress())) {
      throw new ServiceFailureException("Failed to validate ownership of blockchain account.");
    }

    try {
      final var blockChainWallet = BlockChainWallet.of(cmd.getWalletAddress());
      final var updateUserWalletAddressCommand =
          new UpdateUserWalletAddressCommand(cmd.getUserId(), blockChainWallet);
      if (!updateUserWalletAddressPort.update(updateUserWalletAddressCommand)) {
        throw new UnexpectedException("Something was wrong. Please try again");
      }
    } catch (WalletDuplicateException e) {
      throw new ServiceFailureException(
          String.format("Blockchain account \"%s\" is duplicated.", cmd.getWalletAddress()), e);
    }

    return userMapper.mapToUserDto(
        new User(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getNickname(),
            user.getEmail(),
            BlockChainWallet.of(cmd.getWalletAddress()),
            user.getRole()));
  }
}
