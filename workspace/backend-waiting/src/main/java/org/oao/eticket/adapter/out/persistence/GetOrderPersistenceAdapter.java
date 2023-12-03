package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.out.GetOrderPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class GetOrderPersistenceAdapter implements GetOrderPort {

  private final WaitingRepository waitingRepository;

  @Override
  public Long getOrder(String key, Integer userId) {
    return waitingRepository.zRank(key, userId);
  }
}
