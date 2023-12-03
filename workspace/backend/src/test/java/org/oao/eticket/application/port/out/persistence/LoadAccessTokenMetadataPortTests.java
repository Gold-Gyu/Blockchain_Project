package org.oao.eticket.application.port.out.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.oao.eticket.application.domain.model.*;
import org.oao.eticket.application.port.in.CreateAuthTokenUseCase;
import org.oao.eticket.application.port.out.dto.LoadAccessTokenMetadataCommand;
import org.oao.eticket.application.port.out.LoadAccessTokenMetadataPort;
import org.oao.eticket.common.Pair;
import org.oao.eticket.config.AuthRedisContainer;
import org.oao.eticket.exception.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@ExtendWith(AuthRedisContainer.class)
@SpringBootTest(
    properties = {
      "spring.main.lazy-initialization=true",
      "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
    })
class LoadAccessTokenMetadataPortTests {

  private static final User DUMMY_USER =
      new User(
          User.UserID.of(0), null, null, null, null, BlockChainWallet.NULL_WALLET, UserRole.GUEST);

  @Autowired private CreateAuthTokenUseCase createAuthTokenUseCase;
  @Autowired private LoadAccessTokenMetadataPort loadAccessTokenMetadataPort;

  private Pair<Pair<AccessTokenMetadata, String>, Pair<RefreshTokenMetadata, String>>
      createDummyTokenPair() {

    return createAuthTokenUseCase.create(DUMMY_USER);
  }

  @Test
  @DisplayName("토큰이 존재한다면, 토큰을 불러 올 수 있다")
  void whenTokenExists_tokenShallBeLoaded() {
    Assertions.assertDoesNotThrow(
        () -> {
          final var tokenPair = createDummyTokenPair();
          final var accessTokenMetadata = tokenPair.x.x;

          final var cmd =
              new LoadAccessTokenMetadataCommand(
                  DUMMY_USER.getId(), accessTokenMetadata.getTokenId());
          final var loadedAccessTokenMetadata = loadAccessTokenMetadataPort.load(cmd);

          Assertions.assertEquals(loadedAccessTokenMetadata, accessTokenMetadata);
        });
  }

  @Test
  @DisplayName("토큰이 존재하지 않는다면, 예외가 발생한다")
  void whenTokenDoesNotExist_exceptionShallBeThrown() {
    Assertions.assertThrows(
        NoResultException.class,
        () -> {
          final var cmd =
              new LoadAccessTokenMetadataCommand(
                  User.UserID.of(-1), AuthTokenId.of(UUID.randomUUID()));
          loadAccessTokenMetadataPort.load(cmd);
        });
  }
}
