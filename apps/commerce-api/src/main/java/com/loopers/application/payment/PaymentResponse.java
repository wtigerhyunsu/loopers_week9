package com.loopers.application.payment;

import com.loopers.domain.payment.TransactionStatusResponse;

public record PaymentResponse(
    String transactionKey,
    TransactionStatusResponse statusResponse,
    String response
) {
}
