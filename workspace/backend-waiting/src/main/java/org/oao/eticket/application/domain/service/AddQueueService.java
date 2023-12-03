package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.AddQueueUseCase;
import org.oao.eticket.application.port.out.AddQueuePort;
import org.oao.eticket.common.annotation.UseCase;

@UseCase
@RequiredArgsConstructor
public class AddQueueService implements AddQueueUseCase {

  private final AddQueuePort addQueuePort;

  @Override
  public void addQueue(Integer userId, Integer performanceScheduleId) {
    addQueuePort.addQueue(getKey(performanceScheduleId), userId);
  }

  public String getKey(Integer pid) {
    return "Waiting::" + pid;
  }
}
