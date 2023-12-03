package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.mapper.ReservationMapper;
import org.oao.eticket.adapter.out.persistence.repository.ReservationRepository;
import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.domain.model.TicketStatus;
import org.oao.eticket.application.port.out.LoadMyTicketsPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

import java.time.LocalDateTime;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class LoadMyTicketsPersistenceAdapter implements LoadMyTicketsPort {

  private final ReservationRepository reservationRepository;
  private final ReservationMapper reservationMapper;

  @Override
  public List<Reservation> findMyTickets(Integer userId) {
    return reservationRepository
        .findTicketByUserId(userId, LocalDateTime.now(), TicketStatus.SOLDOUT)
        .stream()
        .map(reservationMapper::mapToDomainEntity)
        .toList();
  }
}
