package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.out.PopQueuePort;
import org.oao.eticket.common.annotation.PersistenceAdapter;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

@PersistenceAdapter
@RequiredArgsConstructor
public class PopQueuePersistenceAdapter implements PopQueuePort {

  private final WaitingRepository waitingRepository;

  @Override
  public Set<ZSetOperations.TypedTuple<Integer>> popQueue(String key, Long size) {
    return waitingRepository.zPop(key, size);
  }
}
