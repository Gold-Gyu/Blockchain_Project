package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.User;

public interface LoadUserPort {
  User loadById(User.UserID id);

  User loadByUsername(String username);

  User loadByWallet(byte[] address);

  User loadByWallet(String address);
}
