package org.oao.eticket.adapter.out.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oao.eticket.application.domain.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenMetadataRedisEntity {

  private User.UserID ownerId;
  private String signature;
}
