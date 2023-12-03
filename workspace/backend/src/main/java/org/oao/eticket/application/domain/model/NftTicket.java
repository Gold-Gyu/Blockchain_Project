package org.oao.eticket.application.domain.model;

import lombok.Getter;
import lombok.Value;
import org.web3j.utils.Numeric;

import java.util.Arrays;

@Value
public class NftTicket {

  NftTicketID id;
  User.UserID ownerID;

  public static class NftTicketID {

    @Getter private final byte[] value;

    private NftTicketID(final byte[] value) {
      this.value = new byte[32];
      System.arraycopy(value, 0, this.value, 0, this.value.length);
    }

    @Override
    public String toString() {
      return Numeric.toHexString(this.value);
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(this.value);
    }

    @Override
    public boolean equals(final Object obj) {
      if (!(obj instanceof NftTicketID other)) {
        return false;
      }
      if (value.length != other.value.length) {
        return false;
      }
      for (int i = 0; i < value.length; i++) {
        if (value[i] != other.value[i]) {
          return false;
        }
      }
      return true;
    }

    public static NftTicketID of(final byte[] value) {
      return new NftTicketID(value);
    }
  }
}
