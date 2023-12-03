package org.oao.eticket.application.port.in;

import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;

public interface PopQueueUseCase {
    Set<ZSetOperations.TypedTuple<Integer>> popQueue(Integer pid, Long size);
}
