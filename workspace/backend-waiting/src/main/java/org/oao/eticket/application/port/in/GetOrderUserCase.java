package org.oao.eticket.application.port.in;

public interface GetOrderUserCase {
    Long getOrder(Integer userId, Integer performanceScheduleId);
}
