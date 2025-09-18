package com.loopers.application.payment;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentStateScheduler {
  private final PaymentStateService paymentStateService;

  @Scheduled(fixedRate = 10 * 60 * 1000)
  public void runTaskWithCron() {
    log.info("Cron 스케쥴 작업 실행: {}", LocalDateTime.now());
    paymentStateService.runPendingReconciliation();
  }
}
