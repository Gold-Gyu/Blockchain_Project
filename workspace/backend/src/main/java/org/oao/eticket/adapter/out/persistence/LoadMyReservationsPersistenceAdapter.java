package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.mapper.ReservationMapper;
import org.oao.eticket.adapter.out.persistence.repository.ReservationRepository;
import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.port.out.LoadMyReservationsPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class LoadMyReservationsPersistenceAdapter implements LoadMyReservationsPort {

  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;

  @Override
  public List<Reservation> findMyReservations(Integer userId) {
    return reservationRepository.findByUserJpaEntity_Id(userId).stream()
        .map(reservationMapper::mapToDomainEntity)
        .toList();
  }
}
