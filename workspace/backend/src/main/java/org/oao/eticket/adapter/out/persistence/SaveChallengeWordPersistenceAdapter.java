package org.oao.eticket.adapter.out.persistence;

import java.time.Duration;
import org.oao.eticket.application.port.out.dto.SaveChallengeWordCommand;
import org.oao.eticket.application.port.out.SaveChallengeWordPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;
import org.oao.eticket.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

@PersistenceAdapter
class SaveChallengeWordPersistenceAdapter implements SaveChallengeWordPort {

  private final RedisTemplate<String, String> eticketAuthRedisTemplate;
  private final Duration challengeWordLifetime;

  SaveChallengeWordPersistenceAdapter(
      final RedisTemplate<String, String> eticketAuthRedisTemplate,
      @Value("${eticket.auth.challenge.lifetime}") final int challengeLifetime) {

    this.eticketAuthRedisTemplate = eticketAuthRedisTemplate;
    this.challengeWordLifetime = Duration.ofSeconds(challengeLifetime);
  }

  @Override
  public void save(final SaveChallengeWordCommand cmd) throws ExternalServiceException {
    try {
      final var valueOperations = eticketAuthRedisTemplate.opsForValue();
      final var key = "c-word:" + cmd.getChallengeWordId();
      valueOperations.set(key, cmd.getChallengeWord(), challengeWordLifetime);
    } catch (Exception e) {
      throw new ExternalServiceException("Failed to save challenge word to external storage.", e);
    }
  }
}
