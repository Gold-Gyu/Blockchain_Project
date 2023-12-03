package org.oao.eticket.common;

import lombok.Value;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.security.SignatureException;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

public class EtherUtils {

  @Value
  public static class SiweMessageData {
    String host;
    String account;
    String chainId;
    String nonce;
    Instant issuedAt;
  }

  private static final Pattern SIWE_MESSAGE_MATCHER =
      Pattern.compile(
          "^(?<host>[^ \n]{1,64}) wants you to sign in with your Ethereum account:\n"
              + "(?<account>[0-9a-zA-Z]+)\n+"
              + "URI: (?<uri>[^\n]+)\n"
              + "Version: 1?\n"
              + "Chain ID: (?<chainId>\\d+)\n"
              + "Nonce: (?<nonce>[^\n]+)\n"
              + "Issued At: (?<issuedAt>[.\\-:0-9T]+Z)$");

  public static Optional<SiweMessageData> obtainSiweMessageData(final String siweMessage) {
    final var siweMessageMatcher = SIWE_MESSAGE_MATCHER.matcher(siweMessage);

    if (!siweMessageMatcher.find()) {
      return Optional.empty();
    }

    return Optional.of(
        new SiweMessageData(
            siweMessageMatcher.group("host"),
            siweMessageMatcher.group("account"),
            siweMessageMatcher.group("chainId"),
            siweMessageMatcher.group("nonce"),
            Instant.parse(siweMessageMatcher.group("issuedAt"))));
  }

  public static Sign.SignatureData getSignatureDataFromPersonalSign(final String personalSign)
      throws IllegalArgumentException {

    try {
      final var r = personalSign.substring(0, 66);
      final var s = "0x" + personalSign.substring(66, 130);
      final var v = "0x" + personalSign.substring(130, 132);

      return new Sign.SignatureData(
          Numeric.hexStringToByteArray(v)[0],
          Numeric.hexStringToByteArray(r),
          Numeric.hexStringToByteArray(s));
    } catch (StringIndexOutOfBoundsException e) {
      throw new IllegalArgumentException("Invalid personal sign: " + personalSign, e);
    }
  }

  public static Optional<String> recoverAddressFromPersonalSign(
      final String personalSign, final String messageInHex) throws IllegalArgumentException {

    return recoverAddressFromPersonalSign(personalSign, Numeric.hexStringToByteArray(messageInHex));
  }

  public static Optional<String> recoverAddressFromPersonalSign(
      final String personalSign, final byte[] messageBytes) throws IllegalArgumentException {

    try {
      final var publicKey =
          Sign.signedMessageHashToKey(
              Sign.getEthereumMessageHash(messageBytes),
              getSignatureDataFromPersonalSign(personalSign));

      return Optional.of("0x" + Keys.getAddress(publicKey));
    } catch (SignatureException e) {
      return Optional.empty();
    }
  }
}
