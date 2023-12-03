package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.domain.model.UserRole;
import org.oao.eticket.common.Pair;
import org.oao.eticket.exception.TokenVerificationException;

import java.util.List;

public interface VerifyAccessTokenUseCase {

  Pair<User.UserID, List<UserRole>> verify(String accessJwt) throws TokenVerificationException;
}
