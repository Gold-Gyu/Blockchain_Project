package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "nft_ticket")
@Getter
@Setter
public class NftTicketJpaEntity {

  @Id
  @Column(name = "token_id", nullable = false, columnDefinition = "BINARY(32)")
  byte[] tokenID;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "owner",
      columnDefinition = "binary(20)",
      referencedColumnName = "wallet_address",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  UserJpaEntity owner;
}
