package com.loopers.application.payment;

import com.loopers.domain.payment.CardType;
import com.loopers.domain.payment.PaymentMethod;
import java.math.BigInteger;

public record PaymentGatewayCommand(
    String userId,
    String orderId,
    PaymentMethod paymentMethod,
    CardType cardType,
    String cardNo,
    BigInteger amount) {

  private PaymentGatewayCommand(PaymentCommand command) {
    this(command.pgUserId(),
        command.orderNumber(),
        PaymentMethod.valueOf(command.paymentMethod().name()),
        CardType.valueOf(command.cardType().name()),
        command.cardNo(),
        command.payment());
  }

  public static PaymentGatewayCommand of(PaymentCommand command) {
    return new PaymentGatewayCommand(command);
  }
}
