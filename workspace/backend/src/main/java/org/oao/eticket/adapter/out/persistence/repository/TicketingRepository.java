package org.oao.eticket.adapter.out.persistence.repository;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TicketingRepository {

  //  @Resource(name = "ticketingRedisTemplate")
  private final RedisTemplate<String, Integer> ticketingRedisTemplate;

  /**
   * @desc: Sorted Set 초기화
   */
  public ZSetOperations<String, Integer> opsForZSet() {
    return ticketingRedisTemplate.opsForZSet();
  }

  /**
   * @desc: Sorted Set 요소 삽입
   */
  public void zAdd(String key, Integer userId) {
    opsForZSet().add(key, userId, System.currentTimeMillis());
  }

  /**
   * @desc: Sorted Set 요소 삭제
   */
  public void zRem(String key, Integer userId) {
    opsForZSet().remove(key, userId);
  }

  /**
   * @desc: Sorted Set key 조회
   */
  public Set<String> getKeys(String pattern) {
    return ticketingRedisTemplate.keys(pattern);
  }

  /**
   * @desc: Sorted Set 삭제
   */
  public void delete(String key) {
    ticketingRedisTemplate.delete(key);
  }

  /**
   * @desc: Sorted Set 자료형 사이즈
   */
  public Long zCard(String key) {
    return opsForZSet().size(key);
  }

  /**
   * @desc: Sorted Set 자료형 start ~ end 까지 조회.
   */
  public Set<Integer> zRange(String key, Long start, Long end) {
    return opsForZSet().range(key, start, end);
  }

  /**
   * @desc: Sorted Set 자료형 Value의 현재위치 조회.
   */
  public Long zRank(String key, Integer userId) {
    return opsForZSet().rank(key, userId);
  }
}
