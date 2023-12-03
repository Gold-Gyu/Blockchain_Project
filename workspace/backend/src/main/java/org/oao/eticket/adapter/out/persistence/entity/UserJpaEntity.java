package org.oao.eticket.adapter.out.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.oao.eticket.application.domain.model.UserRole;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserJpaEntity {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  @NotBlank
  private String username;

  @Column(nullable = false)
  @NotBlank
  private String password;

  @Column(nullable = false)
  @NotBlank
  private String nickname;

  @Column(nullable = false)
  @Email
  private String email;

  @Column(nullable = false)
  @Convert(converter = UserRoleConverter.class)
  @NotNull
  private UserRole role;

  @Column(name = "wallet_address", columnDefinition = "binary(20)", unique = true)
  private byte[] walletAddress;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
  private List<NftTicketJpaEntity> nftTickets;
}
