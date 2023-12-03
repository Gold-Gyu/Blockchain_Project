package org.oao.eticket.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.oao.eticket.adapter.out.persistence.mapper.AccessTokenMetadataMapper;
import org.oao.eticket.adapter.out.persistence.mapper.RefreshTokenMetadataMapper;
import org.oao.eticket.application.port.out.dto.SaveAuthTokenMetadataCommand;
import org.oao.eticket.application.port.out.SaveAuthTokenMetadataPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;
import org.oao.eticket.exception.UnexpectedException;
import org.springframework.data.redis.core.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class SaveAuthTokenMetadataPersistenceAdapter implements SaveAuthTokenMetadataPort {

  private final RedisTemplate<String, String> eticketAuthRedisTemplate;
  private final AccessTokenMetadataMapper accessTokenMetadataMapper;
  private final RefreshTokenMetadataMapper refreshTokenMetadataMapper;
  private final ObjectMapper objectMapper;

  @Override
  public void save(final SaveAuthTokenMetadataCommand cmd) {

    eticketAuthRedisTemplate.execute(
        new SessionCallback<>() {
          @Override
          @SuppressWarnings("unchecked")
          public <K, V> Object execute(@NotNull final RedisOperations<K, V> operations) {
            final var valueOperations = (ValueOperations<String, String>) operations.opsForValue();
            final var accessTokenMetadataRedisEntity =
                accessTokenMetadataMapper.mapToRedisEntity(cmd);
            final var refreshTokenMetadataRedisEntity =
                refreshTokenMetadataMapper.mapToRedisEntity(cmd);

            try {
              operations.multi();
              valueOperations.set(
                  "a-token:" + cmd.getOwnerId() + ":" + cmd.getAccessTokenMetadata().getTokenId(),
                  objectMapper.writeValueAsString(accessTokenMetadataRedisEntity),
                  cmd.getAccessTokenLifetime());
              valueOperations.set(
                  "r-token:" + cmd.getOwnerId() + ":" + cmd.getRefreshTokenMetadata().getTokenId(),
                  objectMapper.writeValueAsString(refreshTokenMetadataRedisEntity),
                  cmd.getRefreshTokenLifetime());

              return operations.exec();
            } catch (JsonProcessingException e) {
              throw new UnexpectedException("Unexpected error occurred", e);
            }
          }
        });
  }
}
