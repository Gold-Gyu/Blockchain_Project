package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.RemoveTicketingUseCase;
import org.oao.eticket.application.port.out.RemoveTicketingPort;
import org.oao.eticket.common.annotation.UseCase;

@UseCase
@RequiredArgsConstructor
public class RemoveTicketingService implements RemoveTicketingUseCase {

  private final RemoveTicketingPort removeTicketingPort;

  @Override
  public void removeTicketing(Integer userId, Integer performanceScheduleId) {
    removeTicketingPort.delete(getKey(performanceScheduleId), userId);
  }

  public String getKey(Integer performanceScheduleId) {
    return "Ticketing::" + performanceScheduleId;
  }
}
