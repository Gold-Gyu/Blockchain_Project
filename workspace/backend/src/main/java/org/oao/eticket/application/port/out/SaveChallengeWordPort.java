package org.oao.eticket.application.port.out;

import org.oao.eticket.application.port.out.dto.SaveChallengeWordCommand;
import org.oao.eticket.exception.ExternalServiceException;

public interface SaveChallengeWordPort {
  void save(SaveChallengeWordCommand cmd) throws ExternalServiceException;
}
