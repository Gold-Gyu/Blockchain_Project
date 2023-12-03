package org.oao.eticket.application.domain.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.oao.eticket.application.domain.model.AccessTokenMetadata;
import org.oao.eticket.application.domain.model.AuthTokenId;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.domain.model.UserRole;
import org.oao.eticket.application.port.in.VerifyAccessTokenUseCase;
import org.oao.eticket.application.port.out.dto.LoadAccessTokenMetadataCommand;
import org.oao.eticket.application.port.out.LoadAccessTokenMetadataPort;
import org.oao.eticket.common.Pair;
import org.oao.eticket.common.TokenUtils;
import org.oao.eticket.common.annotation.UseCase;
import org.oao.eticket.exception.NoResultException;
import org.oao.eticket.exception.TokenVerificationException;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@UseCase
public class VerifyAccessTokenService implements VerifyAccessTokenUseCase {

  private final LoadAccessTokenMetadataPort loadAccessTokenMetadataPort;
  private final JWTVerifier jwtVerifier;

  private VerifyAccessTokenService(
      final LoadAccessTokenMetadataPort loadAccessTokenMetadataPort,
      final Algorithm cryptoAlgorithm,
      @Value("${eticket.auth.jwt.issuer}") final String jwtIssuer) {

    this.loadAccessTokenMetadataPort = loadAccessTokenMetadataPort;
    this.jwtVerifier =
        JWT.require(cryptoAlgorithm).withIssuer(jwtIssuer).withAudience(jwtIssuer).build();
  }

  @Override
  public Pair<User.UserID, List<UserRole>> verify(final String accessJwt)
      throws TokenVerificationException {

    DecodedJWT decodedJwt;
    try {
      decodedJwt = jwtVerifier.verify(accessJwt);
    } catch (JWTVerificationException e) {
      throw new TokenVerificationException("JWT verification failure: ", e);
    }

    final var userId = User.UserID.of(decodedJwt.getSubject());
    final var tokenId = AuthTokenId.of(decodedJwt.getClaim("tid").asString());

    AccessTokenMetadata accessTokenMetadata;
    try {
      final var cmd = new LoadAccessTokenMetadataCommand(userId, tokenId);
      accessTokenMetadata = loadAccessTokenMetadataPort.load(cmd);
    } catch (NoResultException e) {
      throw new TokenVerificationException("Token is expired.");
    }

    final var signature = TokenUtils.signatureOf(accessJwt);
    if (!accessTokenMetadata.getSignature().equals(signature)) {
      throw new TokenVerificationException("Token signature is invalid.");
    }

    return Pair.of(
        userId,
        decodedJwt.getClaim("aut").asList(String.class).stream().map(UserRole::of).toList());
  }
}
