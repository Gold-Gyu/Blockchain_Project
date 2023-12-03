package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.port.in.LoadMyReservationsUseCase;
import org.oao.eticket.application.port.out.LoadMyReservationsPort;
import org.oao.eticket.common.annotation.UseCase;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class LoadMyReservationsService implements LoadMyReservationsUseCase {

  private final LoadMyReservationsPort loadMyReservationsPort;

  @Override
  public List<Reservation> loadMyReservations(Integer userId) {
    return loadMyReservationsPort.findMyReservations(userId);
  }
}
