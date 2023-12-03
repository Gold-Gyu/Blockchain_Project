package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oao.eticket.adapter.out.persistence.entity.VenueJpaEntity;
import org.oao.eticket.application.domain.model.Venue;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VenueMapper {

  @Mapping(target = "id", expression = "java(Venue.VenueId.of(jpaEntity.getId()))")
  Venue mapToDomainEntity(VenueJpaEntity jpaEntity);

  @Mapping(target = "id", source = "id.value")
  VenueJpaEntity mapToJpaEntity(Venue model);
}
