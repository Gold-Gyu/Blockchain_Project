package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.entity.ReservationJpaEntity;
import org.oao.eticket.adapter.out.persistence.mapper.ReservationMapper;
import org.oao.eticket.adapter.out.persistence.repository.ReservationRepository;
import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.port.in.dto.CancelMyReservationCommand;
import org.oao.eticket.application.port.out.CancelMyReservationPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class CancelMyReservationPersistenceAdapter implements CancelMyReservationPort {

  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;

  @Override
  public Reservation cancelMyReservation(CancelMyReservationCommand command) {
    ReservationJpaEntity reservationJpaEntity = reservationMapper.mapToJpaEntity(command);
    reservationRepository.save(reservationJpaEntity);
    return reservationMapper.mapToDomainEntity(reservationJpaEntity);
  }
}
