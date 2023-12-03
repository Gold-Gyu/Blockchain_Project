package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oao.eticket.adapter.out.persistence.entity.ConcertHallJpaEntity;
import org.oao.eticket.application.domain.model.ConcertHall;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = VenueMapper.class)
public interface ConcertHallMapper {

  @Mapping(target = "id", expression = "java(ConcertHall.ConcertHallId.of(jpaEntity.getId()))")
  @Mapping(target = "venue", source = "venueJpaEntity")
  ConcertHall mapToDomainEntity(ConcertHallJpaEntity jpaEntity);

  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "venueJpaEntity", source = "venue")
  ConcertHallJpaEntity mapToJpaEntity(ConcertHall model);
}
