package org.oao.eticket.application.port.in;

public interface AddTicketingUseCase {
    void addTicketing(Integer userId, Integer performanceScheduleId);
}
