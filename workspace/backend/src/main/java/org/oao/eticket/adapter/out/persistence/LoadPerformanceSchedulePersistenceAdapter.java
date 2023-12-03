package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.mapper.PerformanceScheduleMapper;
import org.oao.eticket.adapter.out.persistence.repository.PerformanceScheduleRepository;
import org.oao.eticket.application.domain.model.PerformanceSchedule;
import org.oao.eticket.application.port.out.LoadPerformanceSchedulePort;
import org.oao.eticket.common.annotation.PersistenceAdapter;

@PersistenceAdapter
@RequiredArgsConstructor
public class LoadPerformanceSchedulePersistenceAdapter implements LoadPerformanceSchedulePort {

  private final PerformanceScheduleRepository performanceScheduleRepository;
  private final PerformanceScheduleMapper performanceScheduleMapper;

  @Override
  public PerformanceSchedule loadById(Integer id) {
    return performanceScheduleMapper.mapToDomainEntity(
        performanceScheduleRepository.findById(id).get());
  }
}
