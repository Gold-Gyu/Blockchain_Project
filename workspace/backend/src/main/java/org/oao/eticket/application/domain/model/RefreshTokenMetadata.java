package org.oao.eticket.application.domain.model;

import lombok.Getter;

@Getter
public class RefreshTokenMetadata extends AuthTokenMetadata {

  private final AuthTokenId accessTokenId;
  private final String accessTokenSignature;

  public RefreshTokenMetadata(
      final AuthTokenId tokenId,
      final AuthTokenId accessTokenId,
      final String accessTokenSignature,
      final String signature) {

    super(tokenId, signature);
    this.accessTokenId = accessTokenId;
    this.accessTokenSignature = accessTokenSignature;
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof RefreshTokenMetadata _other)) {
      return false;
    }

    return super.equals(_other)
        && _other.accessTokenId.equals(accessTokenId)
        && _other.accessTokenSignature.equals(accessTokenSignature);
  }

  @Override
  public String toString() {
    return super.toString();
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
