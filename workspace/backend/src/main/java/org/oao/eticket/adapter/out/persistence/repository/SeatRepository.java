package org.oao.eticket.adapter.out.persistence.repository;

import org.oao.eticket.adapter.out.persistence.entity.SeatJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.SectionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<SeatJpaEntity, Integer> {
  Optional<List<SeatJpaEntity>> findAllBySectionJpaEntity(SectionJpaEntity sectionJpaEntity);
}
