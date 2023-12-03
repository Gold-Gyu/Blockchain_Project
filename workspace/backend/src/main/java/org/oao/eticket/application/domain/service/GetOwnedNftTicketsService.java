package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.NftTicket;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.in.GetOwnedNftTicketsUseCase;
import org.oao.eticket.application.port.in.dto.NftTicketDto;
import org.oao.eticket.application.port.out.LoadOwnedNftTicketsPort;
import org.oao.eticket.common.annotation.UseCase;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class GetOwnedNftTicketsService implements GetOwnedNftTicketsUseCase {

  private final LoadOwnedNftTicketsPort loadOwnedNftTicketsPort;

  @Override
  public List<NftTicketDto> get(final User.UserID owner) {
    return loadOwnedNftTicketsPort.load(owner).stream()
        .map(
            nftTicket ->
                new NftTicketDto(nftTicket.getId().toString(), nftTicket.getOwnerID().toString()))
        .toList();
  }
}
