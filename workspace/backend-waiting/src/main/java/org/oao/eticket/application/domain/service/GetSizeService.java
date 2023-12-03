package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.GetSizeUseCase;
import org.oao.eticket.application.port.out.GetSizePort;
import org.oao.eticket.common.annotation.UseCase;

@UseCase
@RequiredArgsConstructor
public class GetSizeService implements GetSizeUseCase {

  private final GetSizePort getSizePort;

  @Override
  public Long getSize(Integer pid) {
    return getSizePort.getSize(getKey(pid));
  }

  public String getKey(Integer pid) {
    return "Ticketing::" + pid;
  }
}
