package com.loopers.data_platform.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockDataServer {
  private final LogProducer logProducer;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void send(PlatformSendEvent event) throws InterruptedException {
    log.info("대기중...");
    Thread.sleep(5000);
    log.info("{} 데이터가 플랫폼으로 전송되었습니다. id: {}, payload: {}", event.type(), event.id(), event.payload());
  }

  @Async
  @EventListener
  public void send(UserTrackingData event) {

    if (!event.status()) {
      log.error("데이터 실패 사유 : {}", event.failReason());
      logProducer.send(event.userId(), "데이터 실패 사유 :" + event.failReason());
      return;
    }
    log.info("데이터 전송 : {}", event);
    logProducer.send(event.userId(), "데이터 전송 :" + event.message());
  }


}
