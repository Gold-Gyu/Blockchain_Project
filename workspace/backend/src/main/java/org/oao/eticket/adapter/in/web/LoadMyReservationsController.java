package org.oao.eticket.adapter.in.web;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.domain.model.User;
import org.oao.eticket.application.port.in.LoadMyReservationsUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.infrastructure.security.EticketUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@WebAdapter
@RequiredArgsConstructor
public class LoadMyReservationsController {

  private final LoadMyReservationsUseCase loadMyReservationsUseCase;

  @GetMapping(path = "/api/reservations/{userId}")
  ResponseEntity<List<Reservation>> loadMyReservations(@PathVariable Integer userId, Authentication authentication) {

    if (!(authentication.getPrincipal() instanceof EticketUserDetails userDetails)) {
      throw new RuntimeException("out");
    }
    if (!(User.UserID.of(userId).equals(userDetails.getId()))) {
      throw ApiException.builder()
          .withStatus(HttpStatus.FORBIDDEN)
          .withMessage("Invalid Permission")
          .build();
    }

    List<Reservation> reservations = loadMyReservationsUseCase.loadMyReservations(userId);
    return ResponseEntity.status(200).body(reservations);
  }
}
