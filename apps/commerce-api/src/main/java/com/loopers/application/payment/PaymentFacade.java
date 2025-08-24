package com.loopers.application.payment;


import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.order.orderItem.OrderItemModel;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.PaymentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

  private final PaymentProcessor paymentProcessor;
  private final PaymentHistoryProcessor paymentHistoryProcessor;
  private final OrderRepository orderRepository;
  private final PaymentStrategyFactory paymentFactory;

  private final StockProcessor stockProcessor;

  public PaymentInfo payment(PaymentCommand command) {
    OrderModel orderModel = orderRepository.ofOrderNumber(command.orderNumber());

    orderModel.paymentCheck();

    PaymentStrategy strategy = paymentFactory.getStrategy(PaymentMethod.valueOf(command.method()));
    PaymentModel payment = strategy.process(command, orderModel);

    return PaymentInfo.builder()
        .userId(payment.getUserId())
        .orderNumber(payment.getOrderNumber())
        .orderPrice(payment.getOrderAmount())
        .paymentPrice(payment.getPaymentAmount())
        .description(payment.getDescription())
        .build();
  }

  @Transactional
  public void callback(PaymentCallBackCommand command) {
    PaymentStatus paymentStatus = PaymentStatus.valueOf(command.paymentStatus());
    PaymentModel paymentModel = paymentProcessor.get(command.transactionKey());
    OrderModel orderModel = orderRepository.ofOrderNumber(paymentModel.getOrderNumber());

    // 실패인 경우
    if (paymentStatus == PaymentStatus.FAILED) {
      paymentModel.failed();
      paymentHistoryProcessor.add(paymentModel, command.reason());
      return;
    }

    // 성공인 경우
    // 재고 차감
    for (OrderItemModel orderItem : orderModel.getOrderItems()) {
      Long productId = orderItem.getProductId();
      Long quantity = orderItem.getQuantity();
      stockProcessor.decreaseStock(productId, quantity);
    }
    orderModel.done();
    paymentModel.done();
    paymentHistoryProcessor.add(paymentModel, "결제가 완료되었습니다.");


  }
}
