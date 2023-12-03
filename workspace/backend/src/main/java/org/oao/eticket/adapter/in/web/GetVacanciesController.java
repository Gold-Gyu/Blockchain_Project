package org.oao.eticket.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Seat;
import org.oao.eticket.application.domain.model.Vacancy;
import org.oao.eticket.application.port.in.CheckTicketingPermissionUseCase;
import org.oao.eticket.application.port.in.dto.GetVacanciesCommand;
import org.oao.eticket.application.port.in.GetVacanciesUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.exception.ConcertHallNotFoundException;
import org.oao.eticket.exception.UnexpectedException;
import org.oao.eticket.exception.UserNotFoundException;
import org.oao.eticket.infrastructure.security.EticketUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class GetVacanciesController { // 특정 공연의 특정 구역의 빈 좌석들 불러오기
  record GetPerformanceScheduleVacanciesResponseBody(List<Seat> vacancies) {}

  private final GetVacanciesUseCase getVacanciesUseCase;
  private final CheckTicketingPermissionUseCase checkTicketingPermissionUseCase;

  @Operation(
      summary = "특정 구역에 대한 잔여 좌석 리스트 제공",
      description = "공연장의 특정 구역을 선택 하면 제공 되는 화면 입니다. 사용자가 선택한 구역의 현재 잔여 좌석을 제공 합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "OK 선택한 구역의 현재 잔여 좌석 리스트",
            content =
                @Content(
                    schema =
                        @Schema(
                            implementation = GetPerformanceScheduleVacanciesResponseBody.class))),
        @ApiResponse(
            responseCode = "401",
            description = "NO AUTHORIZED. (권한이 없습니다. 대기열에서 빠져나온 유저의 요청이 아닙니다.)",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "BAD REQUEST. (요청한 API에 해당하는 공연 스케줄 ID 혹은 Section이 존재하지 않습니다.)",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "200-1",
            description = "OK. NO CONTENT. (빈 리스트 - 현재 잔여 좌석 없습니다.)",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
      })
  @GetMapping("/api/schedules/{performanceScheduleId}/sections/{sectionId}/vacancies")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<GetPerformanceScheduleVacanciesResponseBody> GetPerformanceScheduleVacancies(
      @PathVariable Integer performanceScheduleId,
      @PathVariable Integer sectionId,
      final Authentication authentication) {
    try {
      // redis에 들러서 대기열에서 나온 유저인지 확인
      if (!(authentication.getPrincipal() instanceof EticketUserDetails userDetails)) {
        throw ApiException.builder()
            .withStatus(HttpStatus.UNAUTHORIZED)
            .withMessage("Unknown credentials is used.")
            .build();
      }

//      if (!(checkTicketingPermissionUseCase.checkTicketingPermission(
//          userDetails.getId().getValue(), performanceScheduleId))) {
//        throw new UserNotFoundException(String.valueOf(userDetails.getId().getValue()));
//      }
      final var results =
          getVacanciesUseCase.getVacancies(
              GetVacanciesCommand.builder()
                  .performanceScheduleId(performanceScheduleId)
                  .sectionId(sectionId)
                  .build());
      return ResponseEntity.ok(new GetPerformanceScheduleVacanciesResponseBody(results));
    } catch (UnexpectedException e) { // performance Schedule ID나 Section ID 잘못 됨.
      throw ApiException.builder()
          .withStatus(HttpStatus.BAD_REQUEST)
          .withCause(e)
          .withMessage(e.getMessage())
          .build();
    } catch (UserNotFoundException e) { // Section에 SeatClass 등록을 안함
      throw ApiException.builder()
          .withStatus(HttpStatus.UNAUTHORIZED)
          .withCause(e)
          .withMessage(e.getMessage() + "이 유저는 대기열을 거치지 않았습니다.")
          .build();
    } catch (Exception e) {
      // TODO(yoo): exception handling
      // AUTHORIZED (대기열에 등록돼있던 사용자 아님)
      // API BAD REQUEST (performance Schedule과 section의 id가 잘못됨)
      // NO CONTENT (잔여 좌석 없음) or NOT FOUND
      throw e;
    }
  }
}
