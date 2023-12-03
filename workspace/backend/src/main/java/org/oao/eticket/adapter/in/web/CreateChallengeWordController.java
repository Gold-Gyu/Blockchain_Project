package org.oao.eticket.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.CreateChallengeWordUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.exception.ExternalServiceException;
import org.oao.eticket.infrastructure.security.EticketUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;

@WebAdapter
@RequiredArgsConstructor
class CreateChallengeWordController {

  private record Response(String challengeWordId, String challengeWord) {}

  private final CreateChallengeWordUseCase createChallengeWordUseCase;

  @PostMapping("/api/auth/challenge")
  ResponseEntity<?> createChallengeWord() {
    try {
      final var challengeWord = createChallengeWordUseCase.create();

      return ResponseEntity.created(URI.create("/auth/challenge/" + challengeWord.getId()))
          .body(new Response(challengeWord.getId().toString(), challengeWord.getWord()));
    } catch (ExternalServiceException e) {
      throw ApiException.builder()
          .withCause(e)
          .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
          .withMessage("server is unstable")
          .build();
    } catch (Exception e) {
      throw ApiException.builder()
          .withCause(e)
          .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
          .withMessage("something was wrong")
          .build();
    }
  }
}
