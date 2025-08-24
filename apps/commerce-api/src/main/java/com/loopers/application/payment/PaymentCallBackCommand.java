package com.loopers.application.payment;

public record PaymentCallBackCommand(
    String transactionKey,
    String orderId,
    Long amount,
    TransactionStatus status,
    String reason
) {

  public PaymentCallBackCommand(String transactionKey, String orderId, Long amount,
                                String status, String reason) {
    this(transactionKey, orderId, amount, TransactionStatus.valueOf(status), reason);
  }

  public String paymentStatus() {
    return status.name();
  }

}


enum TransactionStatus {
  PENDING, FAILED, SUCCESS
}
