package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.*;
import org.oao.eticket.adapter.out.persistence.entity.PerformanceJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.PerformanceScheduleJpaEntity;
import org.oao.eticket.application.domain.model.Performance;
import org.oao.eticket.application.domain.model.PerformanceSummary;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {
      ConcertHallMapper.class,
      SeatClassMapper.class,
      UserMapper.class,
      PerformanceScheduleMapper.class
    })
public interface PerformanceMapper {
  // 공연 세부 사항 Mapping. - 좌석 등급과 공연 스케줄 리스트 포함
  @Mappings({
    @Mapping(target = "id", expression = "java(Performance.PerformanceId.of(jpaEntity.getId()))"),
    @Mapping(target = "concertHall", source = "concertHallJpaEntity"),
    @Mapping(target = "host", source = "hostJpaEntity"),
    @Mapping(target = "seatClassList", source = "seatClassJpaEntityList"),
    @Mapping(target = "performanceScheduleList", source = "performanceScheduleJpaEntityList")
  })
  Performance mapToDomainEntity(PerformanceJpaEntity jpaEntity);

  // 공연 리스트 Mapping [Hot 공연, Upcoming 공연] - 간략한 정보만
  List<PerformanceSummary> mapToSummaryDomainEntity(List<PerformanceJpaEntity> jpaEntity);

  @Mappings({
    @Mapping(
        target = "id",
        expression = "java(PerformanceSummary.PerformanceSummaryId.of(jpaEntity.getId()))"),
    @Mapping(target = "performanceScheduleList", source = "performanceScheduleJpaEntityList")
  })
  PerformanceSummary mapToSummaryDomainEntity(PerformanceJpaEntity jpaEntity);

  default List<LocalDate> mapToPerformanceDateList(
      List<PerformanceScheduleJpaEntity> scheduleEntities) {
    return scheduleEntities.stream()
        .map(scheduleEntity -> scheduleEntity.getStartDateTime().toLocalDate())
        .collect(Collectors.toList());
  }

  @Mappings({
    @Mapping(target = "id", source = "id.value"),
    @Mapping(target = "concertHallJpaEntity", source = "concertHall"),
    @Mapping(target = "hostJpaEntity", source = "host"),
    @Mapping(target = "seatClassJpaEntityList", ignore = true),
    @Mapping(target = "performanceScheduleJpaEntityList", ignore = true)
  })
  PerformanceJpaEntity mapToJpaEntity(Performance model);
}
