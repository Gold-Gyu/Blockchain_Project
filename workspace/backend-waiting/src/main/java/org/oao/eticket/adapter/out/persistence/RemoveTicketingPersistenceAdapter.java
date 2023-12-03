package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.out.RemoveTicketingPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class RemoveTicketingPersistenceAdapter implements RemoveTicketingPort {

  private final TicketingRepository ticketingRepository;

  @Override
  public void delete(String key, Integer userId) {
    ticketingRepository.zRem(key, userId);
  }
}
