package com.loopers.application.payment;

import com.loopers.domain.point.PointUseCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointUseHandler {
  private final PaymentUsePointHandler handler;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void use(PointUseCommand command) {
    log.info("포인트 사용 이벤트 수신: {}", command);
    handler.use(command.userId(), command.payment());
  }
}
