package org.oao.eticket.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.out.LoadUserPort;
import org.oao.eticket.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class EticketUserDetailsService implements UserDetailsService {

  private final LoadUserPort loadUserPort;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    try {
      return EticketUserDetails.wrap(loadUserPort.loadByUsername(username));
    } catch (UserNotFoundException e) {
      throw new UsernameNotFoundException(e.getMessage(), e);
    }
  }
}
