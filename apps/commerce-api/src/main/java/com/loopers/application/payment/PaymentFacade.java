package com.loopers.application.payment;


import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.infrastructure.payment.PaymentOrderProcessor;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

  private final PaymentProcessor paymentProcessor;
  private final PaymentHistoryProcessor paymentHistoryProcessor;
  private final PaymentStrategyFactory paymentFactory;

  private final PaymentPublisher publisher;
  private final PaymentOrderProcessor processor;

  public PaymentInfo payment(PaymentCommand command) {
    try {
      PaymentStrategy strategy = paymentFactory.getStrategy(PaymentMethod.valueOf(command.method()));
      PaymentModel payment = strategy.process(command);

      publisher.publish(payment.getId(), payment.toString());
      publisher.send(command.userId(), "결제가 생성되었습니다.");
      return PaymentInfo.builder()
          .userId(payment.getUserId())
          .orderNumber(payment.getOrderNumber())
          .orderPrice(payment.getOrderAmount())
          .paymentPrice(payment.getPaymentAmount())
          .description(payment.getDescription())
          .build();
    } catch (Exception e) {
      publisher.fail(command.userId(), e.getMessage());
      throw new CoreException(ErrorType.INTERNAL_ERROR, e.getMessage());
    }

  }

  @Transactional
  public void callback(PaymentCallBackCommand command) {
    PaymentStatus paymentStatus = PaymentStatus.valueOf(command.paymentStatus());
    PaymentModel paymentModel = paymentProcessor.get(command.transactionKey());
    OrderModel orderModel = processor.get(command.orderId());

    // 실패인 경우
    if (paymentStatus == PaymentStatus.FAILED) {
      paymentModel.failed();
      paymentHistoryProcessor.add(paymentModel, command.reason());
      return;
    }

    // 성공인 경우
    // 재고 차감
    publisher.publish(orderModel.getOrderItems());

    paymentModel.done();
    publisher.publish(paymentModel.getOrderNumber());
    paymentHistoryProcessor.add(paymentModel, "결제가 완료되었습니다.");
    publisher.publish(paymentModel.getId(), paymentModel.toString());
  }
}
