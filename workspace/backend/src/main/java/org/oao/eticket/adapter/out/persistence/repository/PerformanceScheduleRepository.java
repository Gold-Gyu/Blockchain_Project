package org.oao.eticket.adapter.out.persistence.repository;

import org.oao.eticket.adapter.out.persistence.entity.ConcertHallJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.PerformanceScheduleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerformanceScheduleRepository
    extends JpaRepository<PerformanceScheduleJpaEntity, Integer> {

  @Query(
      "SELECT ps FROM PerformanceScheduleJpaEntity ps WHERE ps.performanceJpaEntity = "
          + "(SELECT p FROM PerformanceJpaEntity p WHERE FUNCTION('DATE', p.ticketingOpenDateTime) = FUNCTION('DATE', CURRENT_DATE))")
  Optional<List<PerformanceScheduleJpaEntity>>
      loadOpeningPerformanceSchedules(); // 오늘 예매가 오픈 되는 공연 스케줄

  @Query(
      "SELECT ps FROM PerformanceScheduleJpaEntity ps WHERE ps.performanceJpaEntity = "
          + "(SELECT p FROM PerformanceJpaEntity p WHERE FUNCTION('DATE', p.ticketingOpenDateTime) < FUNCTION('DATE', CURRENT_DATE))")
  Optional<List<PerformanceScheduleJpaEntity>>
      loadOpenedPerformanceSchedules(); // 오늘 예매가 오픈 되는 공연 스케줄

  @Query(
      "SELECT ps.performanceJpaEntity.concertHallJpaEntity FROM PerformanceScheduleJpaEntity ps WHERE ps.id = :id")
  Optional<ConcertHallJpaEntity> findConcertHallById(@Param("id") Integer id);
}
