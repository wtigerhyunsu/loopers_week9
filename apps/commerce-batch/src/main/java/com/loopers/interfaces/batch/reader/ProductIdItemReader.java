package com.loopers.interfaces.batch.reader;

import com.loopers.domain.ranking.PeriodResolver;
import com.loopers.infrastructure.metrics.ProductMetricsJpaRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

public class ProductIdItemReader extends AbstractItemCountingItemStreamItemReader<Long> {
  private final ProductMetricsJpaRepository repository;
  private final ZoneId zoneId = ZoneId.of("Asia/Seoul");
  private LocalDate start;
  private LocalDate end;
  private int page = 0;
  private final int pageSize = 1000;
  private Deque<Long> buffer = new ArrayDeque<>();

  public ProductIdItemReader(ProductMetricsJpaRepository repository,
                             @Value("#{jobParameters['mode']}") String mode,
                             @Value("#{jobParameters['date']}") String date) {
    setName("productIdItemReader");
    this.repository = repository;
    PeriodResolver r = new PeriodResolver(zoneId);
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
  protected Long doRead() {
    if (buffer.isEmpty()) {
      List<Long> ids = repository.findDistinctProductIdsByDateBetween(start, end, PageRequest.of(page, pageSize));
      page++;
      if (ids == null || ids.isEmpty()) {
        return null;
      }
      buffer.addAll(ids);
    }
    return buffer.pollFirst();
  }

  @Override
  protected void doOpen() {}

  @Override
  protected void doClose() {
    buffer.clear();
  }
}
