package org.oao.eticket.application.port.out;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

public interface PopQueuePort {
    Set<ZSetOperations.TypedTuple<Integer>> popQueue(String key, Long size);
}
