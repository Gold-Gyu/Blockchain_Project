package org.oao.eticket.application.port.in;

import org.oao.eticket.adapter.in.web.RemoveTicketingController;
import org.springframework.web.bind.annotation.RequestBody;

public interface RemoveTicketingUseCase {
    void removeTicketing(Integer userId, Integer performanceScheduleId);
}
