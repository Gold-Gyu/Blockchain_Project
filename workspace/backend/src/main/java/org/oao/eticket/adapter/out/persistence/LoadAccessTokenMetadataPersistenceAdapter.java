package org.oao.eticket.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.entity.AccessTokenMetadataRedisEntity;
import org.oao.eticket.adapter.out.persistence.mapper.AccessTokenMetadataMapper;
import org.oao.eticket.application.domain.model.AccessTokenMetadata;
import org.oao.eticket.application.port.out.dto.LoadAccessTokenMetadataCommand;
import org.oao.eticket.application.port.out.LoadAccessTokenMetadataPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;
import org.oao.eticket.exception.NoResultException;
import org.oao.eticket.exception.UnexpectedException;
import org.springframework.data.redis.core.RedisTemplate;

@PersistenceAdapter
@RequiredArgsConstructor
class LoadAccessTokenMetadataPersistenceAdapter implements LoadAccessTokenMetadataPort {

  private final RedisTemplate<String, String> eticketAuthRedisTemplate;
  private final AccessTokenMetadataMapper accessTokenMetadataMapper;
  private final ObjectMapper objectMapper;

  @Override
  public AccessTokenMetadata load(final LoadAccessTokenMetadataCommand cmd) {
    final var valueOperations = eticketAuthRedisTemplate.opsForValue();

    final var data = valueOperations.get("a-token:" + cmd.ownerId() + ":" + cmd.accessTokenId());
    if (data == null) {
      throw new NoResultException(
          String.format(
              "An access token %s for user %s not found.", cmd.accessTokenId(), cmd.ownerId()));
    }

    AccessTokenMetadataRedisEntity metadataRedisEntity;
    try {
      metadataRedisEntity = objectMapper.readValue(data, AccessTokenMetadataRedisEntity.class);
    } catch (JsonProcessingException e) {
      throw new UnexpectedException("Error occurred during binding token JSON to entity.", e);
    }

    return accessTokenMetadataMapper.mapToDomainEntity(cmd.accessTokenId(), metadataRedisEntity);
  }
}
