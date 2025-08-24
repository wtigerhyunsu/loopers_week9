package com.loopers.application.catalog.product;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductLikeWarmUp {
  private final ProductWarmupProcessor warmupProcessor;

  @Scheduled(cron = "0 0 1 * * *")
  public void runTaskWithCron() {
    log.info("Cron 스케쥴 작업 실행: {}", LocalDateTime.now());
    warmupProcessor.warmup();
  }
}
