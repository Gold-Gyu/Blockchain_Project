package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.mapper.ReservationMapper;
import org.oao.eticket.adapter.out.persistence.repository.ReservationRepository;
import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.port.out.CreateReservationPort;
import org.oao.eticket.application.port.out.dto.CreateReservationCommand;
import org.oao.eticket.common.annotation.PersistenceAdapter;
import org.springframework.dao.DataIntegrityViolationException;

@PersistenceAdapter
@RequiredArgsConstructor
public class CreateReservationPersistenceAdapter implements CreateReservationPort {

  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;

  @Override
  public Reservation createReservation(final CreateReservationCommand cmd) {
    try {
      final var reservationJpaEntity = reservationMapper.mapToJpaEntity(cmd);
      reservationRepository.save(reservationJpaEntity);
      return reservationMapper.mapToDomainEntity(reservationJpaEntity);
    } catch (DataIntegrityViolationException e) {
      throw new IllegalStateException("Failed to create reservation.", e);
    }
  }
}
