package org.oao.eticket.adapter.in.web;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.oao.eticket.application.port.in.*;
import org.oao.eticket.common.annotation.WebAdapter;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

@WebAdapter
@RequiredArgsConstructor
public class WebSocketController {

  private final GetPidFromTicketingUseCase getPidFromTicketingUseCase;
  private final GetSizeUseCase getSizeUseCase;
  private final GetRangeUseCase getRangeUseCase;
  private final PopQueueUseCase popQueueUseCase;
  private final AddTicketingUseCase addTicketingUseCase;
  private final GetOrderUserCase getOrderUserCase;
  private final SimpMessageSendingOperations sendingOperations;
  public static final long LIMIT_SIZE = 10;

  @Data
  static class WaitingOrder {
    private boolean isMyTurn;
    private long order;
  }

  @Scheduled(fixedDelay = 3000)
  private void schedule() {
    // Ticketing Redis에서 모든 공연 조회
    System.out.println("작동중입니다");
    Set<Integer> pids = getPidFromTicketingUseCase.getPidFromTicketing();
    for (Integer pid : pids) {
      long size = getSizeUseCase.getSize(pid);
      long available = Math.max(LIMIT_SIZE - size, 0); // 가용 인원 수

      System.out.println(size);
      System.out.println(available);

      if (available > 0) { // 인원 수만큼 Waiting에서 빼기
        Set<ZSetOperations.TypedTuple<Integer>> popList = popQueueUseCase.popQueue(pid, available);


        for (ZSetOperations.TypedTuple<Integer> pop : popList) {
          Integer userId = pop.getValue();

          System.out.println("pop" + userId);

          addTicketingUseCase.addTicketing(userId, pid); // Waiting에서 Ticketing으로 옮기기
          WaitingOrder waitingOrder = new WaitingOrder();
          waitingOrder.setMyTurn(true);
          // 대기 끝났다고 알려주기
          sendingOperations.convertAndSend("/sub/userId/" + userId, waitingOrder);
        }
      }
      Set<Integer> waiting = getRangeUseCase.getRange(pid); // Waiting 사람들 가져오기
      for (Integer userId : waiting) {
        long order = getOrderUserCase.getOrder(userId, pid); // 대기 순서 가져오기
        WaitingOrder waitingOrder = new WaitingOrder();
        waitingOrder.setOrder(order);
        waitingOrder.setMyTurn(false);
        // 대기 순서 알려주기
        sendingOperations.convertAndSend("/sub/userId/" + userId, waitingOrder);
      }
    }
  }
}
