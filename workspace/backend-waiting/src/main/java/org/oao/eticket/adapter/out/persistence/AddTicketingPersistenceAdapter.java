package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.out.AddTicketingPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class AddTicketingPersistenceAdapter implements AddTicketingPort {

  private final TicketingRepository ticketingRepository;

  @Override
  public void addTicketing(String key, Integer userId) {
    ticketingRepository.zAdd(key, userId);
  }
}
