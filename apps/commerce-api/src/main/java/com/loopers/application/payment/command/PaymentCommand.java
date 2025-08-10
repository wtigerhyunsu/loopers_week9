package com.loopers.application.payment.command;

import java.math.BigInteger;

public record PaymentCommand(
    String userId,
    String orderNumber,
    BigInteger payment,
    String description
) {
}
