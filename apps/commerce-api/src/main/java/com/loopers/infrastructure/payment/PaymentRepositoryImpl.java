package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.PaymentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
  private final PaymentJpaRepository repository;

  @Override
  public PaymentModel save(PaymentModel paymentModel) {
    return repository.save(paymentModel);
  }

  @Override
  public Optional<PaymentModel> get(String orderNumber) {
    return repository.findByOrderNumber(orderNumber);
  }
}
