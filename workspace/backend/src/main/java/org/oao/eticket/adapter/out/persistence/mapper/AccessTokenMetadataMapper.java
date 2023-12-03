package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oao.eticket.adapter.out.persistence.entity.AccessTokenMetadataRedisEntity;
import org.oao.eticket.application.domain.model.AccessTokenMetadata;
import org.oao.eticket.application.domain.model.AuthTokenId;
import org.oao.eticket.application.port.out.dto.SaveAuthTokenMetadataCommand;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccessTokenMetadataMapper {
  @Mapping(target = "signature", source = "accessTokenMetadata.signature")
  AccessTokenMetadataRedisEntity mapToRedisEntity(SaveAuthTokenMetadataCommand cmd);

  @Mapping(target = "tokenId", source = "accessTokenId")
  AccessTokenMetadata mapToDomainEntity(
      AuthTokenId accessTokenId, AccessTokenMetadataRedisEntity redisEntity);
}
