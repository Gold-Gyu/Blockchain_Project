package org.oao.eticket.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Performance;
import org.oao.eticket.application.port.in.GetPerformanceDetailUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.exception.PerformanceNotFoundException;
import org.oao.eticket.exception.UnexpectedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@WebAdapter
@RequiredArgsConstructor
public class GetPerformanceDetailController {
  record GetPerformanceDetailResponseBody(Performance performance) {}

  private final GetPerformanceDetailUseCase getPerformanceDetailUseCase;

  @Operation(
      summary = "공연 상세 정보",
      description = "특정 공연을 클릭 했을 때 해당 ID를 가진 공연의 상세 정보가 조회 됩니다. 이 곳에서 공연 예매가 가능 합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "OK 공연 상세 불러 오기",
            content =
                @Content(
                    schema = @Schema(implementation = GetPerformanceDetailResponseBody.class))),
        @ApiResponse(
            responseCode = "400",
            description = "BAD REQUEST. performance ID에 해당 하는 공연이 없을 때",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
      })
  @GetMapping(
      value = "/api/performances/{performanceId}",
      produces = "application/json; charset=utf-8")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<GetPerformanceDetailResponseBody> getPerformanceDetail(
      @Valid @PathVariable("performanceId") Integer payload) {
    try {
      final var performance = getPerformanceDetailUseCase.getPerformance(payload);

      return ResponseEntity.ok(new GetPerformanceDetailResponseBody(performance));
    } catch (PerformanceNotFoundException e) { // performance Id 잘못된 경우
      throw ApiException.builder()
          .withStatus(HttpStatus.BAD_REQUEST)
          .withCause(e)
          .withMessage(e.getMessage() + "번에 해당 하는 공연은 존재 하지 않아요.")
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
