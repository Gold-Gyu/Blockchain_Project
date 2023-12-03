package org.oao.eticket.application.port.out;

import org.oao.eticket.application.port.out.dto.UpdateUserWalletAddressCommand;

public interface UpdateUserWalletAddressPort {

  boolean update(UpdateUserWalletAddressCommand cmd);
}
