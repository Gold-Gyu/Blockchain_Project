package org.oao.eticket.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.PerformanceSummary;
import org.oao.eticket.application.port.in.GetHotPerformancesUseCase;
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
public class GetHotPerformancesController {
  record GetHotPerformancesResponseBody(List<PerformanceSummary> hotPerformanceList) {}

  private final GetHotPerformancesUseCase getHotPerformancesUseCase;

  @Operation(
      summary = "인기 TOP 10 공연 List GET",
      description = "메인 화면에 보여 지는, 현재 인기 있는 공연 상위 10개 공연의 간단한 정보의 리스트를 불러 옵니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "OK. 인기 공연 리스트",
            content =
                @Content(schema = @Schema(implementation = GetHotPerformancesResponseBody.class))),
        @ApiResponse(
            responseCode = "200-1",
            description = "OK. (빈 리스트 - 인기 있는 공연 없다)",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
      })
  @GetMapping("/api/performances/hot")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<?> getHotPerformances() {
    try {
      List<PerformanceSummary> list = getHotPerformancesUseCase.getHotPerformanceList();

      return ResponseEntity.ok(new GetHotPerformancesResponseBody(list));
    } catch (PerformanceNotFoundException e) { // 인기 있는 공연이 없을 때.
      throw ApiException.builder()
          .withStatus(HttpStatus.OK)
          .withCause(e)
          .withMessage(e.getMessage())
          .build();
    } catch (IllegalArgumentException e) {
      throw ApiException.builder()
          .withCause(e)
          .withStatus(HttpStatus.BAD_REQUEST)
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
