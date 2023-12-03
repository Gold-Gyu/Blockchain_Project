package org.oao.eticket.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.in.GetOwnedNftTicketsUseCase;
import org.oao.eticket.application.port.in.dto.NftTicketDto;
import org.oao.eticket.common.annotation.WebAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class GetOwnedNftTicketsController {

  private record Request(Integer owner) {}

  private record Response(List<String> tickets) {}

  private final GetOwnedNftTicketsUseCase getOwnedNftTicketsUseCase;

  @GetMapping("/api/nfts")
  public Response getOwnedNftTickets(@Valid @ModelAttribute final Request payload) {
    final var ownerID = User.UserID.of(payload.owner());
    return new Response(
        getOwnedNftTicketsUseCase.get(ownerID).stream()
            .map(ticket -> ticket.getTokenID().toString())
            .toList());
  }
}
