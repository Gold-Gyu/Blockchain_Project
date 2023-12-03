package org.oao.eticket.adapter.out.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oao.eticket.adapter.out.persistence.entity.UserJpaEntity;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.out.dto.CreateUserCommand;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

  @Mapping(target = "id", expression = "java(User.UserID.of(jpaEntity.getId()))")
  @Mapping(
      target = "blockChainWallet",
      expression = "java(BlockChainWallet.of(jpaEntity.getWalletAddress()))")
  User mapToDomainEntity(UserJpaEntity jpaEntity);

  @Mapping(target = "id", expression = "java(domainEntity.getId().getValue())")
  @Mapping(
      target = "walletAddress",
      expression = "java(domainEntity.getBlockChainWallet().getAddress())")
  UserJpaEntity mapToJpaEntity(User domainEntity);

  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "walletAddress", expression = "java(cmd.getWalletAddress())")
  UserJpaEntity mapToJpaEntity(CreateUserCommand cmd);
}
