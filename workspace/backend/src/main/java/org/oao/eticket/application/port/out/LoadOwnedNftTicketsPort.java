package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.NftTicket;
import org.oao.eticket.application.domain.model.User;

import java.util.List;

public interface LoadOwnedNftTicketsPort {

  List<NftTicket> load(User.UserID owner);
}
