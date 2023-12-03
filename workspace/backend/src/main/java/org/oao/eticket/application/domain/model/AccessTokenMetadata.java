package org.oao.eticket.application.domain.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class AccessTokenMetadata extends AuthTokenMetadata {

  public AccessTokenMetadata(final AuthTokenId tokenId, final String signature) {
    super(tokenId, signature);
  }

  @Override
  public String toString() {
    return "AccessTokenMetadata{"
        + "tokenId='"
        + getTokenId()
        + "', signature='"
        + getSignature()
        + "'}";
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTokenId(), getSignature());
  }
}
