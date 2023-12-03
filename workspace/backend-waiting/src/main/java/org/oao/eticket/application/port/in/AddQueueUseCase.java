package org.oao.eticket.application.port.in;

public interface AddQueueUseCase {
    void addQueue(Integer userId, Integer performanceScheduleId);
}
