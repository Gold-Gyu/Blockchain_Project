package org.oao.eticket.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.xml.bind.DatatypeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.util.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;

@Configuration
@RequiredArgsConstructor
public class JWTConfig {

  private final ApplicationContext context;

  private Pair<RSAPrivateKey, RSAPublicKey> loadRSAKeyPairFromClassPath()
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

    final var insecureKeyPath =
        context.getEnvironment().getProperty("eticket.auth.jwt.insecure-key-path");
    if (insecureKeyPath == null) {
      throw new IllegalArgumentException(); // TODO(meo-s): write exception message
    }

    final var pem =
        new ClassPathResource(insecureKeyPath)
            .getContentAsString(StandardCharsets.US_ASCII)
            .replaceAll("\r\n", "\n")
            .replace("-----BEGIN PRIVATE KEY-----\n", "")
            .replace("\n-----END PRIVATE KEY-----\n", "");

    final var keyFactory = KeyFactory.getInstance("RSA");

    final var pkcs8EncodedKeySpec =
        new PKCS8EncodedKeySpec(DatatypeConverter.parseBase64Binary(pem));
    final var privateKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);

    final var publicKeySpec =
        new RSAPublicKeySpec(privateKey.getModulus(), privateKey.getPublicExponent());
    final var publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

    return Pair.of(privateKey, publicKey);
  }

  private Pair<RSAPrivateKey, RSAPublicKey> generateRandomRSAKeyPair()
      throws NoSuchAlgorithmException {

    final var keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
    return Pair.of((RSAPrivateKey) keyPair.getPrivate(), (RSAPublicKey) keyPair.getPublic());
  }

  @Bean
  Algorithm cryptoAlgorithm(@Value("${eticket.auth.jwt.insecure}") final boolean useInsecureKey)
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

    final Pair<RSAPrivateKey, RSAPublicKey> keyPair =
        useInsecureKey ? loadRSAKeyPairFromClassPath() : generateRandomRSAKeyPair();
    return Algorithm.RSA256(keyPair.getSecond(), keyPair.getFirst());
  }
}
