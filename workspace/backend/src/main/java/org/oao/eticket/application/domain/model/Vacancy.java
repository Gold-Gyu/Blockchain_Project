package org.oao.eticket.application.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vacancy {
  Seat seat; // 좌석의 정보
  SeatStatus status; // 좌석의 현재 상태 (구매 가능, 선점 됨, 판매 됨)
}
