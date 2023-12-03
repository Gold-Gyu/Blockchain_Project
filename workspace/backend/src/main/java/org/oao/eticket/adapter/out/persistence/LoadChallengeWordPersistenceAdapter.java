package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.out.LoadChallengeWordPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;
import org.oao.eticket.exception.ExternalServiceException;
import org.oao.eticket.exception.NoResultException;
import org.springframework.data.redis.core.RedisTemplate;

@PersistenceAdapter
@RequiredArgsConstructor
class LoadChallengeWordPersistenceAdapter implements LoadChallengeWordPort {

  private final RedisTemplate<String, String> eticketAuthRedisTemplate;

  @Override
  public String load(final String challengeWordId)
      throws NoResultException, ExternalServiceException {

    final String challengeWord;

    try {
      final var valueOperations = eticketAuthRedisTemplate.opsForValue();
      challengeWord = valueOperations.getAndDelete("c-word:" + challengeWordId);
    } catch (Exception e) {
      throw new ExternalServiceException(e);
    }

    if (challengeWord == null) {
      throw new NoResultException("There is no challenge word of which id is " + challengeWordId + "\n");
    }

    return challengeWord;
  }
}
