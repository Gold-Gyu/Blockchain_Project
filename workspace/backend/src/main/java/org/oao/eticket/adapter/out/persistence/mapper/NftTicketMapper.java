package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oao.eticket.adapter.out.persistence.entity.NftTicketJpaEntity;
import org.oao.eticket.adapter.out.persistence.entity.UserJpaEntity;
import org.oao.eticket.application.domain.model.NftTicket;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    implementationName = "nftTicketImpl")
public interface NftTicketMapper {

  @Mapping(target = "id", source = "nftTicketJpaEntity.tokenID")
  @Mapping(target = "ownerID", ignore = true)
  NftTicket mapToDomainEntity(final NftTicketJpaEntity nftTicketJpaEntity);

  List<NftTicket> mapToDomainEntity(final List<NftTicketJpaEntity> ticketJpaEntities);

  default NftTicket.NftTicketID mapToNftTicketID(final byte[] tokenID) {
    return NftTicket.NftTicketID.of(tokenID);
  }
}
