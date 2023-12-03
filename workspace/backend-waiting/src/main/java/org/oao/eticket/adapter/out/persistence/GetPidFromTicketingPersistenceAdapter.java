package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.out.GetPidFromTicketingPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

import java.util.Set;

@PersistenceAdapter
@RequiredArgsConstructor
public class GetPidFromTicketingPersistenceAdapter implements GetPidFromTicketingPort {

  private final WaitingRepository waitingRepository;

  @Override
  public Set<String> getPidFromTicketing() {
    String pattern = "Waiting::*";
    return waitingRepository.getKeys(pattern);
  }
}
