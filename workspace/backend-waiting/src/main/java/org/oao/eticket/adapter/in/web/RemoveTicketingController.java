package org.oao.eticket.adapter.in.web;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.oao.eticket.application.port.in.RemoveTicketingUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

@WebAdapter
@RequiredArgsConstructor
public class RemoveTicketingController {

  private final RemoveTicketingUseCase removeTicketingUseCase;

  @Value
  static class TicketingDetail {
    @NotNull Integer userId;
    @NotNull Integer performanceScheduleId;
  }

  @DeleteMapping(path = "/api/ticketing")
  public void removeTicketing(@RequestBody TicketingDetail ticketingDetail) {
    removeTicketingUseCase.removeTicketing(ticketingDetail.getUserId(), ticketingDetail.getPerformanceScheduleId());
  }
}
