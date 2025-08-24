package com.loopers.infrastructure.payment.paymentHistory;

import com.loopers.domain.payment.paymentHistory.PaymentHistoryModel;
import com.loopers.domain.payment.paymentHistory.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentHistoryRepositoryImpl implements PaymentHistoryRepository {
  private final PaymentHistoryJpaRepository repository;

  public void add(PaymentHistoryModel model) {
    repository.save(model);
  }
}
