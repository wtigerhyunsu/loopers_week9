package com.loopers.domain.payment;

import java.util.Optional;

public interface PaymentRepository {
  PaymentModel save(PaymentModel paymentModel);

  Optional<PaymentModel> get(String orderNumber);
}
