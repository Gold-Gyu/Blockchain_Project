package org.oao.eticket.application.port.in;

import java.util.Set;

public interface GetRangeUseCase {
    Set<Integer> getRange(Integer pid);
}
