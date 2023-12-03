package org.oao.eticket.application.port.out.dto;

import lombok.Value;
import org.oao.eticket.application.domain.model.AccessTokenMetadata;
import org.oao.eticket.application.domain.model.RefreshTokenMetadata;
import org.oao.eticket.application.domain.model.User;

import java.time.Duration;

@Value
public class SaveAuthTokenMetadataCommand {
  User.UserID ownerId;
  AccessTokenMetadata accessTokenMetadata;
  Duration accessTokenLifetime;
  RefreshTokenMetadata refreshTokenMetadata;
  Duration refreshTokenLifetime;
}
