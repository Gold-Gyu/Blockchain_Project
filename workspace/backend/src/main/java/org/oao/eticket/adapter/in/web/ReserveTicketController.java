package org.oao.eticket.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.oao.eticket.application.domain.model.Reservation;
import org.oao.eticket.application.port.in.dto.ReserveTicketCommand;
import org.oao.eticket.application.port.in.ReserveTicketUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.infrastructure.security.EticketUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@WebAdapter
@RequiredArgsConstructor
public class ReserveTicketController {

  private final ReserveTicketUseCase reserveTicketUseCase;

  private record Request(
      @NotNull Integer performanceScheduleId,
      @NotNull Integer seatId,
      @NotNull Integer paymentAmount) {}

  private record Response(int reservationId) {}

  @Operation(
      summary = "티켓 예매",
      responses = {@ApiResponse(responseCode = "201", description = "티켓 예매 성공")})
  @PostMapping(path = "/api/reservations")
  ResponseEntity<Response> reserveTicket(
      @Valid @RequestBody final Request payload, final Authentication authentication) {

    if (!(authentication.getPrincipal() instanceof EticketUserDetails userDetails)) {
      throw ApiException.builder()
          .withStatus(HttpStatus.UNAUTHORIZED)
          .withMessage("Unknown credentials is used.")
          .build();
    }

    final var cmd =
        ReserveTicketCommand.builder()
            .userId(userDetails.getId())
            .performanceScheduleId(payload.performanceScheduleId())
            .seatId(payload.seatId())
            .paymentAmount(payload.paymentAmount())
            .build();

    final var ticketReservationResult = reserveTicketUseCase.reserveTicket(cmd);
    return ResponseEntity.created(
            URI.create("/reservations/" + ticketReservationResult.getReservationId()))
        .body(new Response(ticketReservationResult.getReservationId()));
  }
}
