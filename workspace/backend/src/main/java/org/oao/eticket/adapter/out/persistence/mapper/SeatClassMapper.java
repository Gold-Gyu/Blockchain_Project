package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.*;
import org.oao.eticket.adapter.out.persistence.entity.SeatClassJpaEntity;
import org.oao.eticket.application.domain.model.SeatClass;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeatClassMapper {

  @Mapping(target = "id", expression = "java(SeatClass.SeatClassId.of(jpaEntity.getId()))")
  SeatClass mapToDomainEntity(SeatClassJpaEntity jpaEntity);

  List<SeatClass> mapToDomainEntity(List<SeatClassJpaEntity> seatClassJpaEntityList);

  @Mapping(target = "id", source = "id.value")
  SeatClassJpaEntity mapToJpaEntity(SeatClass jpaEntity);

  List<SeatClassJpaEntity> mapToJpaEntity(List<SeatClass> seatClassList);
}
