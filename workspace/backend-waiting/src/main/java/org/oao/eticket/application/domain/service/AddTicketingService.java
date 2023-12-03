package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.AddTicketingUseCase;
import org.oao.eticket.application.port.out.AddTicketingPort;
import org.oao.eticket.common.annotation.UseCase;

@UseCase
@RequiredArgsConstructor
public class AddTicketingService implements AddTicketingUseCase {

    private final AddTicketingPort addTicketingPort;

    @Override
    public void addTicketing(Integer userId, Integer performanceScheduleId) {
        addTicketingPort.addTicketing(getKey(performanceScheduleId), userId);
    }

    public String getKey(Integer pid) {
        return "Ticketing::" + pid;
    }
}
