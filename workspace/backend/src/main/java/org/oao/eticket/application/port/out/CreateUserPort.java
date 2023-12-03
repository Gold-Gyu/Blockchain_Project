package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.out.dto.CreateUserCommand;

public interface CreateUserPort {
  User create(CreateUserCommand user);
}
