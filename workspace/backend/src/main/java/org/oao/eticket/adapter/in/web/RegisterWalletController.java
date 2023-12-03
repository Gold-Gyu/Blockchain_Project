package org.oao.eticket.adapter.in.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.RegisterWalletUseCase;
import org.oao.eticket.application.port.in.dto.RegisterWalletCommand;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.exception.ServiceFailureException;
import org.oao.eticket.exception.WalletDuplicateException;
import org.oao.eticket.infrastructure.security.EticketUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.web3j.utils.Numeric;

@WebAdapter
@RequiredArgsConstructor
public class RegisterWalletController {

  private record Request(
      @Pattern(regexp = "^0x[a-zA-Z0-9]{40}$") String walletAddress,
      @Pattern(regexp = "^0x[a-zA-Z0-9]+$") String personalSign) {}

  private record Response(
      int id, String username, String nickname, String email, String walletAddress) {}

  private final RegisterWalletUseCase registerWalletUseCase;

  @PostMapping("/api/users/{userId}/register-wallet")
  ResponseEntity<Response> register(
      @Valid @RequestBody final Request payload,
      @PathVariable final int userId,
      final Authentication authentication) {

    if (!(authentication != null
        && authentication.getPrincipal() instanceof EticketUserDetails userDetails
        && userDetails.getId().getValue() == userId)) {

      throw ApiException.builder()
          .withStatus(HttpStatus.UNAUTHORIZED)
          .withMessage("Permission denied.")
          .build();
    }

    final var userDto =
        registerWalletUseCase.register(
            new RegisterWalletCommand(
                userDetails.getId(),
                payload.personalSign(),
                payload.walletAddress().toLowerCase()));

    return ResponseEntity.status(HttpStatus.OK)
        .header("Location", "/api/users/" + userId)
        .body(
            new Response(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getNickname(),
                userDto.getEmail(),
                Numeric.toHexString(userDto.getWalletAddress())));
  }
}
