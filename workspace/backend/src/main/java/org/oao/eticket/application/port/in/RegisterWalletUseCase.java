package org.oao.eticket.application.port.in;

import org.oao.eticket.application.port.in.dto.RegisterWalletCommand;
import org.oao.eticket.application.port.in.dto.UserDto;

public interface RegisterWalletUseCase {
  UserDto register(RegisterWalletCommand cmd);
}
