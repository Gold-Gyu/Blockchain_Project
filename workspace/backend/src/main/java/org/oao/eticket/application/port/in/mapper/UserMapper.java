package org.oao.eticket.application.port.in.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.oao.eticket.application.domain.model.BlockChainWallet;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.in.dto.UserDto;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    implementationName = "UserDomainEntityMapperImpl")
public interface UserMapper {

  @Mapping(target = "id", source = "id.value")
  @Mapping(target = "password", expression = "java(null)")
  @Mapping(target = "walletAddress", source = "blockChainWallet")
  UserDto mapToUserDto(final User domainEntity);

  default byte[] mapToBytes(final BlockChainWallet wallet) {
    if (wallet == null
        || wallet.getAddress() == null
        || wallet.equals(BlockChainWallet.NULL_WALLET)) {
      return null;
    } else {
      return wallet.getAddress();
    }
  }
}
