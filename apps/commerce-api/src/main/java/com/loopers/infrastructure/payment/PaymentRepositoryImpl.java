package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.PaymentRepository;
import com.loopers.domain.payment.PaymentStatus;
import java.time.ZonedDateTime;
import java.util.List;
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
  public Optional<PaymentModel> get(String transactionId) {
    return repository.findByTransactionId(transactionId);
  }

  @Override
  public List<PaymentModel> findPendingOlderThan(PaymentStatus status, ZonedDateTime threshold) {
    return repository.findPendingOlderThan(status, threshold);
  }

  @Override
  public void updateStatus(String orderId, PaymentStatus paymentStatus) {
    repository.updateStatus(orderId, paymentStatus);
  }

  @Override
  public List<PaymentModel> getAll(String orderId) {
    return repository.findAllByOrderNumber(orderId);
  }


}
