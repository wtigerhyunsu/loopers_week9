package com.loopers.application.payment;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentModel;
import com.loopers.infrastructure.payment.PaymentOrderProcessor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentPointStrategy implements PaymentStrategy {
  private final PaymentProcessor paymentProcessor;
  private final PaymentOrderProcessor processor;
  private final PaymentPublisher publisher;
  private final PaymentHistoryProcessor paymentHistoryProcessor;


  @Override
  @Transactional
  public PaymentModel process(PaymentCommand command) {
    OrderModel orderModel = processor.get(command.orderNumber());

    // 포인트 감소
    publisher.publish(command.userId(), command.payment());
    // 결제 처리
    PaymentModel payment = paymentProcessor.create(new PaymentProcessorVo(
        command.userId(), command.orderNumber(), command.description(),
        null,
        getType().name(),
        command.payment(),
        orderModel.getTotalPrice()
    ));

    // 재고 차감
    publisher.publish(orderModel.getOrderItems());

    payment.done();
    publisher.publish(command.orderNumber());

    paymentHistoryProcessor.add(payment, "결제가 완료되었습니다.");

    return payment;
  }

  @Override
  public PaymentMethod getType() {
    return PaymentMethod.POINT;
  }
}
