package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.paymentHistory.PaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentHistoryProcessor {
  private final PaymentHistoryRepository paymentHistoryRepository;

  public void add(PaymentModel model, String historyReason) {
    paymentHistoryRepository.add(model.asHistory(historyReason));
  }

}
