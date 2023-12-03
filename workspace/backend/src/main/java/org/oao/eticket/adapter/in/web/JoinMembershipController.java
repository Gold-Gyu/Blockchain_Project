package org.oao.eticket.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.oao.eticket.application.port.in.dto.JoinMembershipCommand;
import org.oao.eticket.application.port.in.JoinMembershipUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.exception.UserDuplicateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.net.URISyntaxException;

@WebAdapter
@RequiredArgsConstructor
class JoinMembershipController {

  @Value
  static class JoinMembershipRequestBody {
    @NotBlank
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]{3,11}")
    String username;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{8,20}")
    String password;

    @NotBlank @Email String email;
    @NotBlank String nickname;
  }

  record JoinMembershipResponseBody(
      String username, String nickname, String email, String walletAddress, String role) {}

  private final JoinMembershipUseCase joinMembershipUseCase;
  private final PasswordEncoder passwordEncoder;

  @Operation(
      summary = "회원가입",
      responses = {
        @ApiResponse(
            responseCode = "409",
            description = "이미 존재하는 ID다.",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
      })
  @PostMapping(
      value = "/api/membership/join",
      consumes = "application/json",
      produces = "application/json; charset=utf-8")
  @ResponseStatus(HttpStatus.CREATED)
  ResponseEntity<JoinMembershipResponseBody> joinMembership(
      @Valid @RequestBody JoinMembershipRequestBody payload) throws URISyntaxException {
    try {
      final var user =
          joinMembershipUseCase.join(
              new JoinMembershipCommand(
                  payload.getUsername(),
                  passwordEncoder.encode(payload.getPassword()),
                  payload.getEmail(),
                  payload.getNickname()));

      return ResponseEntity.created(new URI("/users/" + user.getUsername()))
          .body(
              new JoinMembershipResponseBody(
                  user.getUsername(),
                  user.getNickname(),
                  user.getEmail(),
                  user.getBlockChainWallet().toString(),
                  user.getRole().toString()));
    } catch (final UserDuplicateException e) {
      throw ApiException.builder()
          .withCause(e)
          .withStatus(HttpStatus.CONFLICT)
          .withMessage("user duplicate")
          .build();
    }
  }
}
