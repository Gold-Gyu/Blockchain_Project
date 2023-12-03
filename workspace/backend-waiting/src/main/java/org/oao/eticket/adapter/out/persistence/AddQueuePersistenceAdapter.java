package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oao.eticket.application.port.out.AddQueuePort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class AddQueuePersistenceAdapter implements AddQueuePort {

  private final WaitingRepository waitingRepository;

  @Override
  public void addQueue(String key, Integer userId) {
    log.info(key);
    log.info(String.valueOf(userId));
    waitingRepository.zAdd(key, userId);
  }
}
