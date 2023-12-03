package org.oao.eticket.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.GetUserDetailsUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.infrastructure.security.EticketUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.web3j.utils.Numeric;

@WebAdapter
@RequiredArgsConstructor
public class GetUserDetailsController {

  private record Response(
      int id, String username, String nickname, String email, String walletAddress) {}

  private final GetUserDetailsUseCase getUserDetailsUseCase;

  @GetMapping("/api/users/{userId}")
  ResponseEntity<Response> getUserDetails(
      @PathVariable final int userId, final Authentication authentication) {

    if (!(authentication.getPrincipal() instanceof EticketUserDetails eticketUserDetails)) {
      throw ApiException.builder()
          .withStatus(HttpStatus.BAD_REQUEST)
          .withMessage("Unknown credentials is used.")
          .build();
    }

    if (eticketUserDetails.getId().getValue() != userId) {
      throw ApiException.builder()
          .withStatus(HttpStatus.UNAUTHORIZED)
          .withMessage("No permission to use this request.")
          .build();
    }

    final var userDetails = getUserDetailsUseCase.getByUserId(eticketUserDetails.getId());
    return ResponseEntity.ok(
        new Response(
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getNickname(),
            userDetails.getEmail(),
            userDetails.getWalletAddress() != null
                ? Numeric.toHexString(userDetails.getWalletAddress())
                : null));
  }
}
