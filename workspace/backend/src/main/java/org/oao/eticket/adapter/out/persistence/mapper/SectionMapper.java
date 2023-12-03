package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oao.eticket.adapter.out.persistence.entity.SectionJpaEntity;
import org.oao.eticket.application.domain.model.Section;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SectionMapper {

  @Mapping(target = "id", expression = "java(Section.SectionId.of(sectionJpaEntity.getId()))")
  Section mapToDomainEntity(SectionJpaEntity sectionJpaEntity);

  List<Section> mapToDomainEntity(List<SectionJpaEntity> sectionJpaEntities);
}
