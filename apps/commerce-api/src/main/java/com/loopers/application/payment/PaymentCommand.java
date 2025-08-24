package com.loopers.application.payment;

import com.loopers.domain.payment.CardType;
import java.math.BigInteger;
import lombok.Builder;

@Builder
public record PaymentCommand(
    String userId,
    String orderNumber,
    String pgUserId,
    // 어떤 방법으로 결제 할지?
    PaymentMethod paymentMethod,

    CardType cardType,
    String cardNo,

    BigInteger payment,
    String description
) {

  public PaymentCommand(String userId,
                        String orderNumber,
                        String pgUserId,
                        String method,
                        String cardType,
                        String cardNo,
                        BigInteger payment,
                        String description) {
    this(userId, orderNumber, pgUserId, PaymentMethod.valueOf(method), CardType.valueOf(cardType), cardNo, payment, description);
  }

  public PaymentCommand(String userId,
                        String orderNumber,
                        String method,
                        BigInteger payment,
                        String description) {
    this(userId, orderNumber, null, PaymentMethod.valueOf(method), null, null, payment, description);
  }


  enum PaymentMethod {
    CARD,
    POINT,
  }

  String method() {
    return paymentMethod().name();
  }
}
