package org.oao.eticket.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.PerformanceSummary;
import org.oao.eticket.application.port.in.GetUpcomingPerformancesUsecase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.exception.PerformanceNotFoundException;
import org.oao.eticket.exception.UnexpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class GetUpcomingPerformancesController {
  private final GetUpcomingPerformancesUsecase getUpcomingPerformancesUsecase;

  record UpcomingsResponseBody(List<PerformanceSummary> upcomingPerformanceList) {}

  @Operation(
      summary = "예매 오픈 예정 공연 List GET",
      description = "메인 화면에 보여 지는, 아직 예매가 오픈 되지 않은 공연의 간단한 정보의 리스트를 조회 합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "OK 예매 오픈 예정 공연 리스트",
            content = @Content(schema = @Schema(implementation = UpcomingsResponseBody.class))),
        @ApiResponse(
            responseCode = "200-1",
            description = "OK. (빈 리스트 - 예매가 오픈 되지 않은 공연 없다)",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
      })
  @GetMapping("/api/performances/upcoming")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<UpcomingsResponseBody> getUpcomingPerformances() {
    try {
      final var resultList = getUpcomingPerformancesUsecase.getUpcomingPerformances();

      return ResponseEntity.ok(new UpcomingsResponseBody(resultList));
    } catch (PerformanceNotFoundException e) { // 예매 오픈 예정인 공연이 없을 때. (모든 공연이 이미 예매가 오픈 됨)
      throw ApiException.builder()
          .withStatus(HttpStatus.OK)
          .withCause(e)
          .withMessage(e.getMessage())
          .build();
    } catch (UnexpectedException e) {
      e.printStackTrace();
      throw ApiException.builder()
          .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
          .withCause(e)
          .withMessage(e.getMessage())
          .build();
    }
  }
}
