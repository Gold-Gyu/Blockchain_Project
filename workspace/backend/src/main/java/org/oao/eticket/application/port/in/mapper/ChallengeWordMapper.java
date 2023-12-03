package org.oao.eticket.application.port.in.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oao.eticket.application.domain.model.ChallengeWord;
import org.oao.eticket.application.port.out.dto.SaveChallengeWordCommand;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChallengeWordMapper {

  @Mapping(target = "challengeWordId", expression = "java(challengeWord.getId().toString())")
  @Mapping(target = "challengeWord", source = "word")
  SaveChallengeWordCommand mapToSaveCommand(ChallengeWord challengeWord);
}
