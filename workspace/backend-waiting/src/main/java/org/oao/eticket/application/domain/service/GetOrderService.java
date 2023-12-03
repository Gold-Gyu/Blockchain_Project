package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.GetOrderUserCase;
import org.oao.eticket.application.port.out.AddQueuePort;
import org.oao.eticket.application.port.out.GetOrderPort;
import org.oao.eticket.common.annotation.UseCase;
import org.springframework.stereotype.Service;

@UseCase
@RequiredArgsConstructor
public class GetOrderService implements GetOrderUserCase {

  private final GetOrderPort getOrderPort;

  @Override
  public Long getOrder(Integer userId, Integer performanceScheduleId) {
    return getOrderPort.getOrder(getKey(performanceScheduleId), userId);
  }

  public String getKey(Integer pid) {
    return "Waiting::" + pid;
  }
}
