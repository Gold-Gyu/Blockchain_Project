package org.oao.eticket.application.port.in;

import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.in.dto.JoinMembershipCommand;

public interface JoinMembershipUseCase {
  User join(JoinMembershipCommand cmd);
}
