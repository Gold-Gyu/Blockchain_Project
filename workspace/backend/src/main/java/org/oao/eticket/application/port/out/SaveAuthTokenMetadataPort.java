package org.oao.eticket.application.port.out;

import org.oao.eticket.application.port.out.dto.SaveAuthTokenMetadataCommand;

public interface SaveAuthTokenMetadataPort {
  void save(SaveAuthTokenMetadataCommand authToken);
}
