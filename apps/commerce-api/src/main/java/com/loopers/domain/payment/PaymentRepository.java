package com.loopers.domain.payment;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
  PaymentModel save(PaymentModel paymentModel);

  Optional<PaymentModel> get(String orderNumber);

  List<PaymentModel> findPendingOlderThan(PaymentStatus status, ZonedDateTime threshold);

  void updateStatus(String orderId, PaymentStatus paymentStatus);

  List<PaymentModel> getAll(String orderId);
}
