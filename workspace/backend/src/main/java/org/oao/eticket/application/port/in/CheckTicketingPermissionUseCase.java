package org.oao.eticket.application.port.in;

public interface CheckTicketingPermissionUseCase {
  boolean checkTicketingPermission(Integer userId, Integer performanceScheduleId);
}
