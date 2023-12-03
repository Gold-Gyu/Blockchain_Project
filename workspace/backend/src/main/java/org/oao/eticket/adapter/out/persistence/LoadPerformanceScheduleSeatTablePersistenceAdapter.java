package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.entity.PerformanceScheduleJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.SectionJpaEntity;
import org.oao.eticket.adapter.out.persistence.mapper.*;
import org.oao.eticket.adapter.out.persistence.repository.PerformanceScheduleRepository;
import org.oao.eticket.adapter.out.persistence.repository.SeatRepository;
import org.oao.eticket.adapter.out.persistence.repository.SectionRepository;
import org.oao.eticket.application.domain.model.PerformanceScheduleSeatTable;
import org.oao.eticket.application.port.out.LoadPerformanceScheduleSeatTablePort;
import org.oao.eticket.common.annotation.PersistenceAdapter;
import org.oao.eticket.exception.ConcertHallNotFoundException;
import org.oao.eticket.exception.NoResultException;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class LoadPerformanceScheduleSeatTablePersistenceAdapter
    implements LoadPerformanceScheduleSeatTablePort {
  // repository
  private final PerformanceScheduleRepository performanceScheduleRepository;
  private final SectionRepository sectionRepository;
  private final SeatRepository seatRepository;
  // mapper
  private final SeatMapper seatMapper;
  private final PerformanceScheduleMapper performanceScheduleMapper;

  @Override
  public List<PerformanceScheduleSeatTable>
      loadSeatTablesOpenToday() { // 공연 예매가 오픈 되는 공연 스케줄의 좌석 테이블을 생성
    // TMP 일단 모든 스케줄 저장하기
    final var scheduleJpaEntities = performanceScheduleRepository.findAll();

    //     오늘 예매가 오픈 되는 공연 스케줄 가져오기
    //    final var scheduleJpaEntities =
    //        performanceScheduleRepository
    //            .loadOpeningPerformanceSchedules()
    //            .orElseThrow(() -> new NoResultException("오늘 예매가 오픈 되는 공연이 없습니다.")); // query

    List<PerformanceScheduleSeatTable> results = new ArrayList<>();

    // 각 공연 회차의 section List and seat List 가져와 model로
    if (!scheduleJpaEntities.isEmpty()) {
      // for : 오늘 오픈 되는 공연 회차에 대해서
      for (PerformanceScheduleJpaEntity scheduleJpaEntity : scheduleJpaEntities) {
        // Performance Schedule -> Performance -> Concert Hall에서 Section List
        final var sectionJpaEntities =
            sectionRepository
                .findAllByConcertHallJpaEntity(
                    scheduleJpaEntity.getPerformanceJpaEntity().getConcertHallJpaEntity())
                .orElseThrow(
                    () -> new ConcertHallNotFoundException(scheduleJpaEntity.getId().toString()));
        // Section Jpa List -> Section Redis model
        for (SectionJpaEntity sectionJpaEntity : sectionJpaEntities) {
          final var seats =
              seatMapper.mapToDomainEntity(
                  seatRepository
                      .findAllBySectionJpaEntity(sectionJpaEntity)
                      .orElseThrow(() -> new NoResultException("해당 공연장에 좌석 정보가 없어요.")));

          // PerformanceSchedule Seat Table
          PerformanceScheduleSeatTable performanceScheduleSeatTable =
              PerformanceScheduleSeatTable.builder()
                  .performanceScheduleId(scheduleJpaEntity.getId())
                  .sectionId(sectionJpaEntity.getId())
                  .seats(seats)
                  .build();
          // 만든 하나의 구역을 리스트에 추가
          results.add(performanceScheduleSeatTable);
        } // 한 공연 회차에 대한 모든 섹션 끝
      } // 모든 공연 회차 끝
      return results;
    } else {
      throw new NoResultException("오늘 오픈 예정인 공연 없음");
    }
  }
}
