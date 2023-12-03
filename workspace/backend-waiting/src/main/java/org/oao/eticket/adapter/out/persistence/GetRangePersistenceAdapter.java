package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.out.GetRangePort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

import java.util.Set;

@PersistenceAdapter
@RequiredArgsConstructor
public class GetRangePersistenceAdapter implements GetRangePort {

    private final WaitingRepository waitingRepository;

    @Override
    public Set<Integer> getRange(String key) {
        return waitingRepository.zRange(key);
    }
}
