package com.loopers.infrastructure.payment.paymentHistory;

import com.loopers.domain.payment.paymentHistory.PaymentHistoryModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryJpaRepository extends JpaRepository<PaymentHistoryModel, Long> {
  Optional<PaymentHistoryModel> findByPaymentId(Long paymentId);
}
