package org.oao.eticket.application.port.out.dto;

import lombok.Value;
import org.oao.eticket.application.domain.model.BlockChainWallet;
import org.oao.eticket.application.domain.model.User;

@Value
public class UpdateUserWalletAddressCommand {

  User.UserID targetUserID;
  BlockChainWallet newBlockChainWallet;
}
