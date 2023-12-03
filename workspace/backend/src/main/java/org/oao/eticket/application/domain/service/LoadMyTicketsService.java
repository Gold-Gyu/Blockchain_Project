package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.port.in.LoadMyTicketsUseCase;
import org.oao.eticket.application.port.out.LoadMyTicketsPort;
import org.oao.eticket.common.annotation.UseCase;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class LoadMyTicketsService implements LoadMyTicketsUseCase {

  private final LoadMyTicketsPort loadMyTicketsPort;

  @Override
  public List<Reservation> loadMyTickets(Integer userId) {
    return loadMyTicketsPort.findMyTickets(userId);
  }
}
