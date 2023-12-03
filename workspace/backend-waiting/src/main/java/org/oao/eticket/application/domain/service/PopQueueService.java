package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.PopQueueUseCase;
import org.oao.eticket.application.port.out.PopQueuePort;
import org.oao.eticket.common.annotation.UseCase;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

@UseCase
@RequiredArgsConstructor
public class PopQueueService implements PopQueueUseCase {

  private final PopQueuePort popQueuePort;

  @Override
  public Set<ZSetOperations.TypedTuple<Integer>> popQueue(Integer pid, Long size) {
    return popQueuePort.popQueue(getKey(pid), size);
  }

  public String getKey(Integer pid) {
    return "Waiting::" + pid;
  }
}
