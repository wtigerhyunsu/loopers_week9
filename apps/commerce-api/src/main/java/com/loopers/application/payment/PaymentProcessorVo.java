package com.loopers.application.payment;

import java.math.BigInteger;


public record PaymentProcessorVo(
    String userId,
    String orderNumber,
    String description,
    String transactionKey,
    String paymentMethod,
    BigInteger payment,
    BigInteger totalPrice
) {
}
