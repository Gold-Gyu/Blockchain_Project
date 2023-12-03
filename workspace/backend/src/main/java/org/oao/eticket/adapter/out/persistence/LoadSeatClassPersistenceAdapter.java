package org.oao.eticket.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.mapper.SeatClassMapper;
import org.oao.eticket.adapter.out.persistence.repository.SectionAndSeatClassRelationRepository;
import org.oao.eticket.application.domain.model.SeatClass;
import org.oao.eticket.application.port.out.dto.LoadSeatClassCommand;
import org.oao.eticket.application.port.out.LoadSeatClassPort;
import org.oao.eticket.common.annotation.PersistenceAdapter;
import org.oao.eticket.exception.SeatClassNotFoundException;

@PersistenceAdapter
@RequiredArgsConstructor
public class LoadSeatClassPersistenceAdapter implements LoadSeatClassPort {
  private final SectionAndSeatClassRelationRepository sectionAndSeatClassRelationRepository;
  private final SeatClassMapper seatClassMapper;

  @Override
  public SeatClass loadSeatClass(LoadSeatClassCommand loadSeatClassCommand) {
    return seatClassMapper.mapToDomainEntity(
        sectionAndSeatClassRelationRepository
            .findSeatClassBySectionAndPerformance(
                loadSeatClassCommand.getSectionId(),
                loadSeatClassCommand.getPerformanceScheduleId())
            .orElseThrow(() -> new SeatClassNotFoundException("각 구역에 좌석 등급이 할당 되지 않았습니다.")));
  }
}
