package org.oao.eticket.application.domain.service;

import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.domain.model.PerformanceScheduleSeatTable;
import org.oao.eticket.application.port.in.SaveVacanciesToRedisUseCase;
import org.oao.eticket.application.port.out.LoadPerformanceScheduleSeatTablePort;
import org.oao.eticket.application.port.out.SaveVacanciesRedisPort;
import org.oao.eticket.common.annotation.UseCase;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class SaveVacanciesToRedisSchedulerService implements SaveVacanciesToRedisUseCase {
  private final LoadPerformanceScheduleSeatTablePort loadPerformanceScheduleSeatTablePort;
  private final SaveVacanciesRedisPort saveVacanciesRedisPort;

  //    @Scheduled(cron = "0 0 0 * * ?") // 매일 00시 00분 00초에 실행
  @Override
  public List<PerformanceScheduleSeatTable> saveVacanciesToRedis() {
    // 1. 저장할 공연 객체 DB에서 가져오기 (특정 시간에 그날 예매 열리는 공연 회차의 section과 좌석 정보)
    final var seatTables = loadPerformanceScheduleSeatTablePort.loadSeatTablesOpenToday();
    // 2. 레디스에 저장. (List<PerformanceScheduleSeatTable>)     schedule Id랑 section Id 조합해서 키로, List?
    for (PerformanceScheduleSeatTable seatTable : seatTables) {
      System.out.println(seatTable.getPerformanceScheduleId() + " " + seatTable.getSectionId());
      saveVacanciesRedisPort.saveSeatTable(seatTable);
    }
    return seatTables;
  }
}
