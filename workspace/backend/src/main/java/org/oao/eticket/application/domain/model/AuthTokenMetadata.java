package org.oao.eticket.application.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public abstract class AuthTokenMetadata {

  private final AuthTokenId tokenId;
  private final String signature;

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof AuthTokenMetadata otherAuthTokenMetadata)) {
      return false;
    }

    return tokenId.equals(otherAuthTokenMetadata.tokenId)
        && signature.equals(otherAuthTokenMetadata.signature);
  }

  @Override
  public String toString() {
    return "AuthTokenMetadata{" + "tokenId='" + tokenId + "', signature='" + signature + "'}";
  }

  @Override
  public int hashCode() {
    return Objects.hash(tokenId, signature);
  }
}
