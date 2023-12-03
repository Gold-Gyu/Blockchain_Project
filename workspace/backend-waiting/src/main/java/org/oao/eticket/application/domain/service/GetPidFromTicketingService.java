package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.GetPidFromTicketingUseCase;
import org.oao.eticket.application.port.out.GetPidFromTicketingPort;
import org.oao.eticket.common.annotation.UseCase;

import java.util.Set;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class GetPidFromTicketingService implements GetPidFromTicketingUseCase {

  private final GetPidFromTicketingPort getPidFromTicketing;

  @Override
  public Set<Integer> getPidFromTicketing() {
    Set<String> keys = getPidFromTicketing.getPidFromTicketing();
    System.out.println(keys.toString());
    return keys.stream().map(this::convert).collect(Collectors.toSet());
  }

  public Integer convert(String key) {
    return Integer.parseInt(key.split("::")[1]);
  }
}
