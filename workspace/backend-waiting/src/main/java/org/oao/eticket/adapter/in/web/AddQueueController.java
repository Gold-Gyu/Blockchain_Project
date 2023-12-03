package org.oao.eticket.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.oao.eticket.application.port.in.AddQueueUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class AddQueueController {

  private final AddQueueUseCase addQueueUseCase;

  @Value
  static class WaitingDetail {
    @NotNull Integer userId;
    @NotNull Integer performanceScheduleId;
  }

  @PostMapping(path = "/api/waiting")
  public void addQueue(@RequestBody WaitingDetail waitingDetail) {
    addQueueUseCase.addQueue(waitingDetail.getUserId(), waitingDetail.getPerformanceScheduleId());
  }
}
