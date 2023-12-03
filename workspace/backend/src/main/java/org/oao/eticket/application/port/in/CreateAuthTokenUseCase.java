package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.AccessTokenMetadata;
import org.oao.eticket.application.domain.model.RefreshTokenMetadata;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.common.Pair;

public interface CreateAuthTokenUseCase {
  Pair<Pair<AccessTokenMetadata, String>, Pair<RefreshTokenMetadata, String>> create(
      User targetUser);
}
