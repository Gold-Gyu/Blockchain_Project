package org.oao.eticket.adapter.out.persistence.repository;

import org.oao.eticket.adapter.out.persistence.entity.ReservationJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.UserJpaEntity;
import org.oao.eticket.application.domain.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationJpaEntity, Integer> {
  @Query("select r from ReservationJpaEntity r where r.userJpaEntity.id = ?1")
  List<ReservationJpaEntity> findByUserJpaEntity_Id(@NonNull Integer id);

  @Query(
      """
              select r from ReservationJpaEntity r
              where r.userJpaEntity.id = ?1 and r.performanceScheduleJpaEntity.startDateTime > ?2 and r.ticketStatus =?3""")
  List<ReservationJpaEntity> findTicketByUserId(
      @NonNull Integer id,
      @NonNull LocalDateTime startDateTime,
      @NonNull TicketStatus ticketStatus);
}
