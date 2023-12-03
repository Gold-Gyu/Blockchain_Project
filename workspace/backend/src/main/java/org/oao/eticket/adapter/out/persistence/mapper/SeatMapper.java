package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oao.eticket.adapter.out.persistence.entity.SeatJpaEntity;
import org.oao.eticket.application.domain.model.Seat;
import org.oao.eticket.application.domain.model.SeatStatus;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeatMapper {

  Seat mapToDomainEntity(SeatJpaEntity seatJpaEntity);

  List<Seat> mapToDomainEntity(List<SeatJpaEntity> seatJpaEntities);

  SeatJpaEntity mapToJpaEntity(Seat seat);
}
