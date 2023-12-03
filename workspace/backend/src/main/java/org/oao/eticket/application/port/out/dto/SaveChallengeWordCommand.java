package org.oao.eticket.application.port.out.dto;

import lombok.Value;
import org.oao.eticket.application.domain.model.User;

@Value
public class SaveChallengeWordCommand {
  String challengeWordId;
  String challengeWord;
}
