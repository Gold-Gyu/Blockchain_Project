package org.oao.eticket.application.domain.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.oao.eticket.application.domain.model.BlockChainWallet;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.domain.model.UserRole;
import org.oao.eticket.config.AuthRedisContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(AuthRedisContainer.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = {
      "spring.main.lazy-initialization=true",
      "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
    })
class CreateAuthTokenServiceTests {

  private static final User DUMMY_USER =
      new User(
          User.UserID.of(0), null, null, null, null, BlockChainWallet.NULL_WALLET, UserRole.GUEST);

  @Autowired private CreateAuthTokenService createAuthTokenService;

  @Test
  @DisplayName("유저 ID가 null이 아니라면, 토큰이 생성된다")
  void whenUserIdIsNotNull_tokenShallBeCreated() {
    Assertions.assertDoesNotThrow(
        () -> {
          createAuthTokenService.create(DUMMY_USER);
        });
  }
}
