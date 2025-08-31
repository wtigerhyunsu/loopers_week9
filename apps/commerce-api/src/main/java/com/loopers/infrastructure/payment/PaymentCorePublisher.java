package com.loopers.infrastructure.payment;

import com.loopers.application.payment.PaymentPublisher;
import com.loopers.data_platform.PlatformSendEvent;
import com.loopers.data_platform.UserTrackingData;
import com.loopers.domain.catalog.product.stock.StockDecreaseCommand;
import com.loopers.domain.order.orderItem.OrderItemModel;
import com.loopers.domain.payment.PaymentOrderConfirmCommand;
import com.loopers.domain.point.PointUseCommand;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCorePublisher implements PaymentPublisher {
  private final ApplicationEventPublisher publisher;

  public void publish(String orderNumber) {
    publisher.publishEvent(new PaymentOrderConfirmCommand(orderNumber));
  }

  public void publish(Long paymentId, String result) {
    log.info("결제 정보에서 데이터 플랫폼으로 데이터를 전송합니다.");
    publisher.publishEvent(new PlatformSendEvent(
        "결제", paymentId, result
    ));
  }

  // 포인트
  @Override
  public void publish(String userId, BigInteger payment) {
    publisher.publishEvent(new PointUseCommand(userId, payment));
  }

  // 재고
  @Override
  public void publish(List<OrderItemModel> orderItems) {
    publisher.publishEvent(new StockDecreaseCommand(orderItems));
  }

  @Override
  public void send(String userId, String message) {
    publisher.publishEvent(
        new UserTrackingData(
            userId,
            "PAYMENT_CREATE",
            message,
            true,
            ZonedDateTime.now()
        )
    );
  }

  @Override
  public void fail(String userId, String failMessage) {
    publisher.publishEvent(
        new UserTrackingData(
            userId,
            "PAYMENT_CREATE",
            false,
            failMessage,
            ZonedDateTime.now()
        )
    );
  }



}
