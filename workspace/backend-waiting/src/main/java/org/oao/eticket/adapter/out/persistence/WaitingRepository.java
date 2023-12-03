package org.oao.eticket.adapter.out.persistence;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class WaitingRepository {

//  @Resource(name = "waitingStorage")
  private final RedisTemplate<String, Integer> waitingStorage;

  /**
   * @desc: Sorted Set 초기화
   */
  public ZSetOperations<String, Integer> opsForZSet() {
    return waitingStorage.opsForZSet();
  }

  /**
   * @desc: Sorted Set 요소 삽입
   */
  public void zAdd(String key, int userId) {
    opsForZSet().add(key, userId, System.currentTimeMillis());
  }

  /**
   * @desc: Sorted Set 요소 삭제
   */
  public Set<ZSetOperations.TypedTuple<Integer>> zPop(String key, Long size) {
    return opsForZSet().popMin(key, size);
  }

  /**
   * @desc: Sorted Set key 조회
   */
  public Set<String> getKeys(String pattern) {
    return waitingStorage.keys(pattern);
  }

  /**
   * @desc: Sorted Set 삭제
   */
  public void delete(String key) {
    waitingStorage.delete(key);
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
  public Set<Integer> zRange(String key) {
    return opsForZSet().range(key, 0, -1);
  }

  /**
   * @desc: Sorted Set 자료형 Value의 현재위치 조회.
   */
  public Long zRank(String key, Integer userId) {
    return opsForZSet().rank(key, userId);
  }
}
