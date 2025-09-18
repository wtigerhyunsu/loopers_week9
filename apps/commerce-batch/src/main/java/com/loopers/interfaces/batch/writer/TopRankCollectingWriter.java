package com.loopers.interfaces.batch.writer;

import com.loopers.application.ranking.ProductScore;
import com.loopers.domain.ranking.AggregationRepository;
import com.loopers.domain.ranking.AggregationRepository.RankEntry;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class TopRankCollectingWriter implements ItemWriter<ProductScore>, StepExecutionListener {
  private final AggregationRepository repository;
  private final String mode;
  private final LocalDate start;
  private final LocalDate end;
  private final List<ProductScore> buffer = new ArrayList<>();

  public TopRankCollectingWriter(AggregationRepository repository,
                                 @Value("#{jobParameters['mode']}") String mode,
                                 @Value("#{jobParameters['date']}") String date) {
    this.repository = repository;
    this.mode = mode == null ? "weekly" : mode.toLowerCase();
    var r = new com.loopers.domain.ranking.PeriodResolver(ZoneId.of("Asia/Seoul"));
    if (date != null && !date.isBlank()) {
      LocalDate d = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
      if ("weekly".equals(this.mode)) {
        var range = r.weekOf(d);
        this.start = range.start();
        this.end = range.end();
      } else {
        var range = r.monthOf(d);
        this.start = range.start();
        this.end = range.end();
      }
    } else {
      if ("weekly".equals(this.mode)) {
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
  public void write(Chunk<? extends ProductScore> items) {
    buffer.addAll(items.getItems());
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {}

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    List<ProductScore> ordered = buffer.stream()
        .sorted(Comparator.comparingDouble(ProductScore::score).reversed()
            .thenComparingLong(ProductScore::productId))
        .limit(100)
        .collect(Collectors.toList());

    List<RankEntry> entries = new ArrayList<>();
    double prev = Double.NaN;
    int rank = 0;
    for (ProductScore ps : ordered) {
      if (Double.isNaN(prev) || Double.compare(ps.score(), prev) != 0) {
        rank += 1;
        prev = ps.score();
      }
      entries.add(new RankEntry(ps.productId(), Math.round(ps.score()), rank));
    }

    if ("weekly".equals(mode)) {
      repository.replaceWeekly(start, end, entries);
    } else {
      repository.replaceMonthly(start, end, entries);
    }
    buffer.clear();
    return ExitStatus.COMPLETED;
  }
}
