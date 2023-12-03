package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.*;
import org.oao.eticket.adapter.out.persistence.entity.PerformanceScheduleJpaEntity;
import org.oao.eticket.application.domain.model.PerformanceSchedule;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {PerformanceMapper.class})
public interface PerformanceScheduleMapper {

  // 공연 세부 사항을 보는 경우를 제외한 일반적 경우
  @Mapping(target = "performance", source = "performanceJpaEntity")
  PerformanceSchedule mapToDomainEntity(PerformanceScheduleJpaEntity performanceScheduleJpaEntity);

  // 공연 세부 사항을 조회할 때 공연 별 스케줄을 조회
  @Named("scheduleList")
  @Mapping(target = "performance", ignore = true)
  PerformanceSchedule mapToDomainEntityInDetail(
      PerformanceScheduleJpaEntity performanceScheduleJpaEntity);

  @IterableMapping(qualifiedByName = "scheduleList")
  List<PerformanceSchedule> mapToDomainEntityList(
      List<PerformanceScheduleJpaEntity> performanceScheduleJpaEntityList);

  //  @Mapping(target = "performanceJpaEntity", source = "performance")
  @Mapping(target = "performanceJpaEntity", ignore = true)
  PerformanceScheduleJpaEntity mapToJpaEntity(PerformanceSchedule performanceSchedule);

  List<PerformanceScheduleJpaEntity> mapToJpaEntity(
      List<PerformanceSchedule> performanceScheduleJpaEntityList);
}
