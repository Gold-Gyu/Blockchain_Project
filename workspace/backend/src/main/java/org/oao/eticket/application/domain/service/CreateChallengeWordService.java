package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.ChallengeWord;
import org.oao.eticket.application.port.in.mapper.ChallengeWordMapper;
import org.oao.eticket.application.port.in.CreateChallengeWordUseCase;
import org.oao.eticket.application.port.out.SaveChallengeWordPort;
import org.oao.eticket.common.annotation.UseCase;

@UseCase
@RequiredArgsConstructor
public class CreateChallengeWordService implements CreateChallengeWordUseCase {

  private final ChallengeWordMapper challengeWordMapper;
  private final SaveChallengeWordPort saveChallengeWordPort;

  @Override
  public ChallengeWord create() {
    final var challengeWord = ChallengeWord.random(16);
    saveChallengeWordPort.save(challengeWordMapper.mapToSaveCommand(challengeWord));

    return challengeWord;
  }
}
