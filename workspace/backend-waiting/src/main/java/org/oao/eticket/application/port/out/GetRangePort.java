package org.oao.eticket.application.port.out;

import java.util.Set;

public interface GetRangePort {
    Set<Integer> getRange(String key);
}
