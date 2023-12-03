package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.mapper.ReservationMapper;
import org.oao.eticket.adapter.out.persistence.repository.ReservationRepository;
import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.port.out.LoadReservationPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class LoadReservationPersistenceAdapter implements LoadReservationPort {

  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;

  @Override
  public Reservation loadById(Integer id) {
    return reservationMapper.mapToDomainEntity(reservationRepository.findById(id).get());
  }
}
