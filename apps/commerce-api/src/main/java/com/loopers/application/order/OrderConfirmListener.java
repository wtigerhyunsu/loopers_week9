package com.loopers.application.order;

import com.loopers.domain.payment.PaymentOrderConfirmCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderConfirmListener {
  private final OrderProcessor orderProcessor;

  @TransactionalEventListener
  public void handler(PaymentOrderConfirmCommand command) {
    log.info("{} 주문이 완료 처리 되었습니다. ", command.orderNumber());
    orderProcessor.confirm(command.orderNumber());
  }
}
