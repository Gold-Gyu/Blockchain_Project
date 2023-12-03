package org.oao.eticket.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.out.LoadChallengeWordPort;
import org.oao.eticket.application.port.out.LoadUserPort;
import org.oao.eticket.common.EtherUtils;
import org.oao.eticket.exception.ExternalServiceException;
import org.oao.eticket.exception.NoResultException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.web3j.utils.Numeric;

import java.util.List;

@RequiredArgsConstructor
public class PersonalSignAuthenticationProvider implements AuthenticationProvider {

  private final LoadUserPort loadUserPort;
  private final LoadChallengeWordPort loadChallengeWordPort;

  private PersonalSignAuthenticationToken castToPersonalSignAuthenticationToken(
      final Authentication authentication) throws AuthenticationException {

    if (!(authentication instanceof PersonalSignAuthenticationToken authenticationToken)) {
      throw new BadAuthenticationRequestException(
          "Unsupported authentication: " + authentication.getClass().getName());
    }

    if (!(authenticationToken.getPrincipal() instanceof String)) {
      throw new BadAuthenticationRequestException(
          "A provided user principal is not wallet address.");
    }

    return authenticationToken;
  }

  private String signerOf(final String personalSign, byte[] siweMessageBytes)
      throws AuthenticationException {

    try {
      return EtherUtils.recoverAddressFromPersonalSign(personalSign, siweMessageBytes)
          .orElseThrow(
              () ->
                  new BadAuthenticationRequestException(
                      "Personal Sign & SIWE message pair is illegal."));
    } catch (IllegalArgumentException e) {
      throw new BadAuthenticationRequestException("Authentication failed.", e);
    }
  }

  private EtherUtils.SiweMessageData obtainSiweMessageData(
      final PersonalSignAuthenticationToken authenticationToken) throws AuthenticationException {

    final var siweMessageBytes = Numeric.hexStringToByteArray(authenticationToken.getSiweMessage());
    final var signerAddress = signerOf(authenticationToken.getPersonalSign(), siweMessageBytes);

    if (!authenticationToken.getPrincipal().equals(signerAddress)) {
      throw new UnexpectedAuthenticationException("Authentication failed. Account mismatched.");
    }

    return EtherUtils.obtainSiweMessageData(new String(siweMessageBytes))
        .orElseThrow(() -> new BadAuthenticationRequestException("SIWE message is ill-formed."));
  }

  @Override
  public Authentication authenticate(final Authentication unknownAuthentication)
      throws AuthenticationException {

    final var authenticationToken = castToPersonalSignAuthenticationToken(unknownAuthentication);
    final var siweMessageData = obtainSiweMessageData(authenticationToken);

    try {
      final var user = loadUserPort.loadByWallet(siweMessageData.getAccount());
      final var challengeWord = loadChallengeWordPort.load(authenticationToken.getChallenge());

      if (!challengeWord.equals(siweMessageData.getNonce())) {
        throw new BadAuthenticationRequestException("Authentication failed.");
      }

      final var usernamePasswordAuthenticationToken =
          UsernamePasswordAuthenticationToken.authenticated(
              EticketUserDetails.wrap(user),
              null,
              List.of(EticketGrantedAuthority.wrap(user.getRole())));

      usernamePasswordAuthenticationToken.setDetails(authenticationToken.getDetails());
      authenticationToken.eraseCredentials();

      return usernamePasswordAuthenticationToken;
    } catch (NoResultException e) {
      throw new BadAuthenticationRequestException("Authentication failed.", e);
    } catch (ExternalServiceException e) {
      throw new AuthenticationServiceException("Server is not healthy. Please try again later.", e);
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(PersonalSignAuthenticationToken.class);
  }
}
