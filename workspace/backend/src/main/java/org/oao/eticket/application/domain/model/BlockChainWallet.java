package org.oao.eticket.application.domain.model;

import lombok.Getter;
import org.web3j.utils.Numeric;

@Getter
public class BlockChainWallet {

  public static final BlockChainWallet NULL_WALLET = new BlockChainWallet((byte[]) null);

  private final byte[] address;

  private String addressInHex = null;

  private BlockChainWallet(final byte[] address) {
    this.address = address;
  }

  private BlockChainWallet(final String addressInHex) {
    this.address = Numeric.hexStringToByteArray(addressInHex);
    this.addressInHex = addressInHex;
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof BlockChainWallet)) {
      return false;
    }

    final var otherAddress = ((BlockChainWallet) other).address;
    if (otherAddress == null) {
      return false;
    }

    for (int i = 0; i < address.length; ++i) {
      if (address[i] != otherAddress[i]) {
        return false;
      }
    }

    return true;
  }

  @Override
  public String toString() {
    if (addressInHex == null) {
      addressInHex = address != null ? Numeric.toHexString(address) : "0x00000000000000000000";
    }
    return addressInHex;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  public static BlockChainWallet of(final byte[] address) {
    return new BlockChainWallet(address);
  }

  public static BlockChainWallet of(final String addressInHex) {
    return new BlockChainWallet(addressInHex);
  }
}
