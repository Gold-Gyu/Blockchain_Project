package org.oao.eticket.application.domain.model;

import java.util.UUID;
import lombok.Value;
import org.apache.commons.lang3.RandomStringUtils;

@Value
public class ChallengeWord {

  ChallengeWordId id;
  String word;

  public static ChallengeWord random(final int wordLength) {
    return new ChallengeWord(
        ChallengeWordId.of(UUID.randomUUID()), RandomStringUtils.randomAlphanumeric(wordLength));
  }

  @Value
  public static class ChallengeWordId {

    UUID value;

    public static ChallengeWordId of(final UUID value) {
      return new ChallengeWordId(value);
    }

    public static ChallengeWordId of(final String value) {
      return new ChallengeWordId(UUID.fromString(value));
    }

    @Override
    public String toString() {
      return value.toString();
    }
  }
}
