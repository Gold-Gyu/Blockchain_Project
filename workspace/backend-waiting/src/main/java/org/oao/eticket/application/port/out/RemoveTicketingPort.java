package org.oao.eticket.application.port.out;

public interface RemoveTicketingPort {
    void delete(String key, Integer userId);
}
