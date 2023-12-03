package org.oao.eticket.application.port.out;

import org.oao.eticket.application.domain.model.AccessTokenMetadata;
import org.oao.eticket.application.port.out.dto.LoadAccessTokenMetadataCommand;

public interface LoadAccessTokenMetadataPort {
  AccessTokenMetadata load(LoadAccessTokenMetadataCommand cmd);
}
