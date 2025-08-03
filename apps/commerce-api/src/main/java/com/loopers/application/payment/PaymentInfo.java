package com.loopers.application.payment;

import java.math.BigInteger;
import lombok.Builder;

@Builder
public record PaymentInfo(
    String userId,
    String orderNumber,
    BigInteger orderPrice, // 주문 금액
    BigInteger paymentPrice, //결제 금액
    String description
) {
}
