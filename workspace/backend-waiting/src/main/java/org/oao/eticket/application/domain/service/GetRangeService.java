package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.GetRangeUseCase;
import org.oao.eticket.application.port.out.GetRangePort;
import org.oao.eticket.common.annotation.UseCase;

import java.util.Set;

@UseCase
@RequiredArgsConstructor
public class GetRangeService implements GetRangeUseCase {

  private final GetRangePort getRangePort;

  @Override
  public Set<Integer> getRange(Integer pid) {
    return getRangePort.getRange(getKey(pid));
  }

  public String getKey(Integer pid) {
    return "Waiting::" + pid;
  }
}
