package org.oao.eticket.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.Section;
import org.oao.eticket.application.port.in.CheckTicketingPermissionUseCase;
import org.oao.eticket.application.port.in.GetSectionsUseCase;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.exception.*;
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
public class GetSectionsController { // 예매 대기열이 끝난 후, 특정 공연에 대한 공연장 정보

  record GetSectionsResponseBody(List<Section> sectionList) {}

  private final GetSectionsUseCase getSectionsUseCase;
  private final CheckTicketingPermissionUseCase checkTicketingPermissionUseCase;

  @Operation(
      summary = "특정 공연 회차에 대한 공연장 구역 리스트 제공",
      description =
          "예매 대기열에서 빠져 나온 사용자가 처음 예매 화면에 진입 했을 때 제공할 API 입니다. \n 특정 공연 회차에 진행 되는 공연장의 구역 정보가 리스트로 제공 됩니다. ",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "OK 선택한 공연 회차의 공연장 구역 정보 리스트",
            content = @Content(schema = @Schema(implementation = GetSectionsResponseBody.class))),
        @ApiResponse(
            responseCode = "401",
            description = "NO AUTHORIZED. (권한이 없습니다. 대기열에서 빠져 나온 유저의 요청이 아닙니다.)",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "BAD REQUEST. (요청한 API에 해당 하는 공연 스케줄 ID가 존재 하지 않습니다.)",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description =
                "NOT FOUND. (해당 공연이 개최 되는 콘서트 홀에 section 정보가 없거나 각 section에 좌석 등급이 할당 되지 않았습니다.)",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
      })
  @GetMapping("/api/schedules/{performanceScheduleId}/sections")
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<GetSectionsResponseBody> getSections(
      @PathVariable("performanceScheduleId") Integer performanceScheduleId,
      final Authentication authentication) {
    try {
      //  redis에 들러서 대기열에서 나온 유저인지 확인
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
      // use case릁 통해 MySql에서 특정 공연의 상세 정보 가져 오기
      final var sections = getSectionsUseCase.getSections(performanceScheduleId);

      return ResponseEntity.ok(new GetSectionsResponseBody(sections));
    } catch (ConcertHallNotFoundException e) { // DB에 공연에 해당 하는 콘서트 홀 연결 안함
      throw ApiException.builder()
          .withStatus(HttpStatus.BAD_REQUEST)
          .withCause(e)
          .withMessage(e.getMessage() + "번 공연 회차는 존재 하지 않아요.")
          .build();
    } catch (SectionNotFoundException e) { // DB에 공연에 해당 하는 콘서트 홀에 Section을 등록 안함
      throw ApiException.builder()
          .withStatus(HttpStatus.NOT_FOUND)
          .withCause(e)
          .withMessage(e.getMessage() + "번 공연 회차에 해당 하는 콘서트 홀에 구역 정보가 존재 하지 않아요.")
          .build();
    } catch (SeatClassNotFoundException e) { // Section에 SeatClass 등록을 안함
      throw ApiException.builder()
          .withStatus(HttpStatus.NOT_FOUND)
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
      e.printStackTrace();
      throw ApiException.builder()
          .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
          .withCause(e)
          .withMessage(e.getMessage())
          .build();
    }
  }
}
