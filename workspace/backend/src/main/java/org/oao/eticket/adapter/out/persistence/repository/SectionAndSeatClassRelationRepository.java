package org.oao.eticket.adapter.out.persistence.repository;

import org.oao.eticket.adapter.out.persistence.entity.PerformanceJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.SeatClassJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.SectionAndSeatClassRelationJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.SectionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionAndSeatClassRelationRepository
    extends JpaRepository<SectionAndSeatClassRelationJpaEntity, Integer> {

  @Query(
      "SELECT s.seatClassJpaEntity "
          + "FROM SectionAndSeatClassRelationJpaEntity s "
          + "WHERE s.sectionJpaEntity.id = :sectionId "
          + "AND s.seatClassJpaEntity.performanceJpaEntity = "
          + "(SELECT ps.performanceJpaEntity FROM PerformanceScheduleJpaEntity ps WHERE ps.id = :performanceScheduleId)")
  Optional<SeatClassJpaEntity> findSeatClassBySectionAndPerformance(
      @Param("sectionId") Integer sectionId,
      @Param("performanceScheduleId") Integer performanceScheduleId);
}
