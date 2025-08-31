package com.loopers.application.payment;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.TransactionStatusResponse;
import com.loopers.infrastructure.payment.PaymentOrderProcessor;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentCardStrategy implements PaymentStrategy {
  private final PaymentGatewayPort paymentGatewayPort;
  private final PaymentProcessor paymentProcessor;
  private final PaymentOrderProcessor orderProcessor;

  private final PaymentHistoryProcessor paymentHistoryProcessor;

  @Override
  public PaymentModel process(PaymentCommand command) {
    OrderModel orderModel = orderProcessor.get(command.orderNumber());
    // PG사 요청
    PaymentResponse response = paymentGatewayPort.send(PaymentGatewayCommand.of(command));

    if (response.statusResponse() == TransactionStatusResponse.FAILED) {
      throw new CoreException(ErrorType.INTERNAL_ERROR, response.response());
    }

    PaymentModel payment = paymentProcessor.create(new PaymentProcessorVo(
        command.userId(),
        command.orderNumber(),
        command.description(),
        response.transactionKey(),
        getType().name(),
        command.payment(),
        orderModel.getTotalPrice()
    ));

    paymentHistoryProcessor.add(payment, "결제 대기중입니다.");
    return payment;
  }

  @Override
  public PaymentMethod getType() {
    return PaymentMethod.CARD;
  }
}
