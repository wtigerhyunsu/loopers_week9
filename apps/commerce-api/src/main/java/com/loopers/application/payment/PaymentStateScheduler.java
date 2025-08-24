package com.loopers.application.payment;

import com.loopers.domain.payment.OrderResponse;
import com.loopers.domain.payment.OrderResponse.TransactionResponse;
import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.PaymentRepository;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.infrastructure.payment.PaymentGatewayPortImpl;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentStateScheduler {
  private final PaymentGatewayPortImpl paymentGatewayPortImpl;
  private final PaymentRepository paymentRepository;
  private final PaymentHistoryProcessor paymentHistoryProcessor;


  @Transactional
  @Scheduled(fixedRate = 10 * 60 * 1000)
  public void runTaskWithCron() {
    log.info("Cron 스케쥴 작업 실행: {}", LocalDateTime.now());

    ZonedDateTime plusMinutes = ZonedDateTime.now().plusMinutes(5);

    List<PaymentModel> allPayments = paymentRepository.findPendingOlderThan(
        PaymentStatus.PENDING, plusMinutes
    );

    if (allPayments.isEmpty()) {
      log.info("변경될 결제가 존재하지 않습니다.");
      return;
    }

    Map<String, List<PaymentModel>> paymentsByOrderNumber = allPayments.stream()
        .collect(Collectors.groupingBy(PaymentModel::getOrderNumber));

    for (Entry<String, List<PaymentModel>> entry : paymentsByOrderNumber.entrySet()) {
      String orderId = entry.getKey();
      OrderResponse orderResponse = paymentGatewayPortImpl.get(orderId);
      List<TransactionResponse> transactions = orderResponse.transactions();
      List<PaymentModel> paymentModels = entry.getValue();
      for (PaymentModel paymentModel : paymentModels) {
        TransactionResponse transactionResponse = transactions.stream()
            .filter(payment -> paymentModel.getTransactionId().equals(payment.transactionKey()))
            .findFirst().orElse(null);
        if (transactionResponse == null) {
          continue;
        }
        String status = transactionResponse.status().name();
        String reason = transactionResponse.reason();
        paymentModel.changeStatus(status);

        paymentHistoryProcessor.add(paymentModel, reason);

      }


    }
  }

}
