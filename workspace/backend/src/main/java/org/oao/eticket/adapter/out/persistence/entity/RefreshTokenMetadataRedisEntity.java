package org.oao.eticket.adapter.out.persistence.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oao.eticket.application.domain.model.AuthTokenId;

@Getter
@Setter
@NoArgsConstructor
public class RefreshTokenMetadataRedisEntity {

  private String signature;
  private AuthTokenId accessTokenId;
  private String accessTokenSignature;
}
