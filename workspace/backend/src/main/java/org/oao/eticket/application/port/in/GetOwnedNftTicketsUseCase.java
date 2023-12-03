package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.in.dto.NftTicketDto;

import java.util.List;

public interface GetOwnedNftTicketsUseCase {

  List<NftTicketDto> get(User.UserID owner);
}
