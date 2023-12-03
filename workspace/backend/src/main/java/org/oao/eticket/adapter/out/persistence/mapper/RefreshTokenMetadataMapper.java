package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oao.eticket.adapter.out.persistence.entity.RefreshTokenMetadataRedisEntity;
import org.oao.eticket.application.port.out.dto.SaveAuthTokenMetadataCommand;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RefreshTokenMetadataMapper {

  @Mapping(target = "signature", source = "refreshTokenMetadata.signature")
  @Mapping(target = "accessTokenId", source = "refreshTokenMetadata.accessTokenId")
  @Mapping(target = "accessTokenSignature", source = "refreshTokenMetadata.signature")
  RefreshTokenMetadataRedisEntity mapToRedisEntity(SaveAuthTokenMetadataCommand cmd);
}
