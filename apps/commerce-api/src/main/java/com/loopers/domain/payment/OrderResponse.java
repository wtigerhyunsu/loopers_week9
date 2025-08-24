package com.loopers.domain.payment;

import java.util.List;

public record OrderResponse(
    String orderId,
    List<TransactionResponse> transactions
) {

  public record TransactionResponse(
      String transactionKey,
      TransactionStatusResponse status,
      String reason

  ) {
  }
}
