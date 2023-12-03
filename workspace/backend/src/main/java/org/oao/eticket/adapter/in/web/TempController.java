package org.oao.eticket.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.adapter.out.persistence.repository.PerformanceScheduleRepository;
import org.oao.eticket.application.domain.model.PerformanceScheduleSeatTable;
import org.oao.eticket.application.port.in.SaveVacanciesToRedisUseCase;
import org.oao.eticket.application.port.out.LoadPerformanceScheduleSeatTablePort;
import org.oao.eticket.common.annotation.WebAdapter;
import org.oao.eticket.exception.SeatTableDuplicatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class TempController {
  // 스케줄러로 만든 서비스 API 테스트 해보려고 만든 애 (오늘 예매 오픈 하는 공연이 redis에 저장 됨)
  private final SaveVacanciesToRedisUseCase saveVacanciesToRedisUseCase;

  @GetMapping("api/test")
  List<PerformanceScheduleSeatTable> test() {
    try {
      return saveVacanciesToRedisUseCase.saveVacanciesToRedis();
    } catch (SeatTableDuplicatedException e) {
      throw ApiException.builder()
          .withStatus(HttpStatus.BAD_REQUEST)
          .withCause(e)
          .withMessage(e.getMessage())
          .build();
    }
  }
}
