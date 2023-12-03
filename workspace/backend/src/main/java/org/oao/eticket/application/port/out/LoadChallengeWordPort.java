package org.oao.eticket.application.port.out;

import org.oao.eticket.exception.ExternalServiceException;
import org.oao.eticket.exception.NoResultException;

public interface LoadChallengeWordPort {

  String load(String challengeWordId) throws NoResultException, ExternalServiceException;
}
