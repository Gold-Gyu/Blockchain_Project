package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.out.GetSizePort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class GetSizePersistenceAdapter implements GetSizePort {

    private final TicketingRepository ticketingRepository;

    @Override
    public Long getSize(String key) {
        return ticketingRepository.zCard(key);
    }
}
