package org.oao.eticket.adapter.out.persistence.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.entity.PerformanceJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.PerformanceScheduleJpaEntity;
import org.oao.eticket.adapter.out.persistence.mapper.PerformanceMapper;
import org.oao.eticket.application.domain.model.Performance;
import org.oao.eticket.application.domain.model.PerformanceSummary;
import org.oao.eticket.application.port.out.LoadHotPerformancesPort;
import org.oao.eticket.application.port.out.LoadPerformanceDetailPort;
import org.oao.eticket.application.port.out.LoadUpcomingPerformancesPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;
import org.oao.eticket.exception.PerformanceNotFoundException;
import org.oao.eticket.exception.UnexpectedException;

import java.util.Comparator;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class PerformanceRepository
    implements LoadPerformanceDetailPort, LoadHotPerformancesPort, LoadUpcomingPerformancesPort {

  private final PerformanceMapper performanceMapper;

  @PersistenceContext private final EntityManager entityManager;

  @Override
  public Performance loadById(Performance.PerformanceId performanceId) {
    try {
      final var performanceJpaEntity =
          entityManager
              .createQuery(
                  """
                          SELECT p
                          FROM PerformanceJpaEntity p
                          WHERE p.id=:performanceId
                          """,
                  PerformanceJpaEntity.class)
              .setParameter("performanceId", performanceId.getValue())
              .getSingleResult();
      sortSchedules(performanceJpaEntity);

      return performanceMapper.mapToDomainEntity(performanceJpaEntity);
    } catch (NoResultException e) {
      throw new PerformanceNotFoundException(String.valueOf(performanceId.getValue()), e);
    } catch (Exception e) {
      throw new UnexpectedException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public List<PerformanceSummary> loadHotPerformances() {
    try {
      final var queryResults =
          entityManager
              .createQuery(
                  """
                          SELECT p
                          FROM PerformanceJpaEntity p
                          ORDER BY p.id DESC
                          """,
                  PerformanceJpaEntity.class)
              .setMaxResults(10)
              .getResultList();
      // 빈 결과물이면 없다고 띄우기
      if (queryResults.isEmpty()) {
        throw new NoResultException();
      }
      for (PerformanceJpaEntity performanceJpaEntity : queryResults) {
        sortSchedules(performanceJpaEntity);
      }

      return performanceMapper.mapToSummaryDomainEntity(queryResults);
    } catch (NoResultException e) {
      throw new PerformanceNotFoundException("인기있는 공연이 존재하지 않습니다.", e);
    } catch (Exception e) {
      throw new UnexpectedException(e.getMessage(), e.getCause());
    }
  }

  @Override
  public List<PerformanceSummary> loadUpcomings() {
    try {
      final var queryResults =
          entityManager
              .createQuery(
                  """
                      SELECT p
                      FROM PerformanceJpaEntity p
                      WHERE p.ticketingOpenDateTime > CURRENT_TIMESTAMP
                      ORDER BY p.ticketingOpenDateTime ASC
                      """,
                  PerformanceJpaEntity.class)
              .setMaxResults(10)
              .getResultList();
      if (queryResults.isEmpty()) {
        throw new NoResultException();
      }
      for (PerformanceJpaEntity performanceJpaEntity : queryResults) {
        sortSchedules(performanceJpaEntity);
      }
      return performanceMapper.mapToSummaryDomainEntity(queryResults);
    } catch (NoResultException e) {
      throw new PerformanceNotFoundException("예매 오픈 예정인 공연이 존재 하지 않습니다.", e);
    } catch (IllegalArgumentException e) {
      // QUERY 오타
      throw e;
    } catch (Exception e) {
      throw new UnexpectedException(e.getMessage(), e.getCause());
    }
  }

  public void sortSchedules(PerformanceJpaEntity performanceJpaEntity) { // 공연 회차를 시간 순으로 정렬
    performanceJpaEntity
        .getPerformanceScheduleJpaEntityList()
        .sort(Comparator.comparing(PerformanceScheduleJpaEntity::getStartDateTime));
  }
}
