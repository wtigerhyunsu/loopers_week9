package com.loopers.interfaces.batch.processor;

import com.loopers.application.ranking.ProductScore;
import com.loopers.domain.ranking.ScoreCalculator;
import com.loopers.infrastructure.metrics.ProductMetricsJpaRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class ProductScoreItemProcessor implements ItemProcessor<Long, ProductScore> {
  private final ProductMetricsJpaRepository repository;
  private final ScoreCalculator calculator;
  private final LocalDate start;
  private final LocalDate end;

  public ProductScoreItemProcessor(ProductMetricsJpaRepository repository,
                                   ScoreCalculator calculator,
                                   @Value("#{jobParameters['mode']}") String mode,
                                   @Value("#{jobParameters['date']}") String date) {
    this.repository = repository;
    this.calculator = calculator;
    var r = new com.loopers.domain.ranking.PeriodResolver(ZoneId.of("Asia/Seoul"));
    if (date != null && !date.isBlank()) {
      LocalDate d = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
      if ("weekly".equalsIgnoreCase(mode)) {
        var range = r.weekOf(d);
        this.start = range.start();
        this.end = range.end();
      } else {
        var range = r.monthOf(d);
        this.start = range.start();
        this.end = range.end();
      }
    } else {
      if ("weekly".equalsIgnoreCase(mode)) {
        var range = r.lastClosedWeek();
        this.start = range.start();
        this.end = range.end();
      } else {
        var range = r.lastClosedMonth();
        this.start = range.start();
        this.end = range.end();
      }
    }
  }

  @Override
  public ProductScore process(Long productId) {
    var rows = repository.findByProductIdAndDateBetween(productId, start, end);
    java.util.Collection<long[]> values = rows.stream()
        .map(m -> new long[]{nz(m.getViews()), nz(m.getLikes()), nz(m.getSales()), nz(m.getAmount())})
        .toList();
    double sum = calculator.dailyScaledSum(values);
    return new ProductScore(productId, sum);
  }

  private long nz(Long v) { return v == null ? 0L : v; }
}
