package com.loopers.infrastructure.ranking;

import com.loopers.domain.metrics.ProductMetricsModel;
import com.loopers.domain.ranking.AggregationRepository;
import com.loopers.domain.ranking.MonthlyRankEntity;
import com.loopers.domain.ranking.MonthlyRankId;
import com.loopers.domain.ranking.WeeklyRankEntity;
import com.loopers.domain.ranking.WeeklyRankId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loopers.infrastructure.metrics.ProductMetricsJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RankingAggregationJpaRepository implements AggregationRepository {
  private final ProductMetricsJpaRepository metrics;
  private final WeeklyRankMvJpaRepository weekly;
  private final MonthlyRankMvJpaRepository monthly;

  public RankingAggregationJpaRepository(ProductMetricsJpaRepository metrics, WeeklyRankMvJpaRepository weekly, MonthlyRankMvJpaRepository monthly) {
    this.metrics = metrics;
    this.weekly = weekly;
    this.monthly = monthly;
  }

  @Override
  public Map<Long, Map<LocalDate, long[]>> loadDailyMetrics(LocalDate start, LocalDate end) {
    Map<Long, Map<LocalDate, long[]>> daily = new HashMap<>();
    List<ProductMetricsModel> rows = metrics.findByDateBetween(start, end);
    for (ProductMetricsModel r : rows) {
      long pid = r.getProductId();
      LocalDate d = r.getDate();
      long views = r.getViews() == null ? 0L : r.getViews();
      long likes = r.getLikes() == null ? 0L : r.getLikes();
      long sales = r.getSales() == null ? 0L : r.getSales();
      long amount = r.getAmount() == null ? 0L : r.getAmount();
      daily.computeIfAbsent(pid, k -> new HashMap<>())
          .computeIfAbsent(d, k -> new long[4]);
      long[] arr = daily.get(pid).get(d);
      arr[0] += views;
      arr[1] += likes;
      arr[2] += sales;
      arr[3] += amount;
    }
    return daily;
  }

  @Override
  @Transactional
  public void replaceWeekly(LocalDate start, LocalDate end, List<AggregationRepository.RankEntry> entries) {
    replace("mv_product_rank_weekly", start, end, entries);
  }

  @Override
  @Transactional
  public void replaceMonthly(LocalDate start, LocalDate end, List<AggregationRepository.RankEntry> entries) {
    replace("mv_product_rank_monthly", start, end, entries);
  }

  private void replace(String table, LocalDate start, LocalDate end, List<AggregationRepository.RankEntry> entries) {
    if ("mv_product_rank_weekly".equals(table)) {
      weekly.deletePeriod(start, end);
      List<WeeklyRankEntity> batch = new ArrayList<>();
      for (AggregationRepository.RankEntry e : entries) {
        WeeklyRankId id = new WeeklyRankId(start, e.productId());
        batch.add(new WeeklyRankEntity(id, end, e.score(), e.rank(), LocalDateTime.now()));
      }
      weekly.saveAll(batch);
    } else {
      monthly.deletePeriod(start, end);
      List<MonthlyRankEntity> batch = new ArrayList<>();
      for (AggregationRepository.RankEntry e : entries) {
        MonthlyRankId id = new MonthlyRankId(start, e.productId());
        batch.add(new MonthlyRankEntity(id, end, e.score(), e.rank(), LocalDateTime.now()));
      }
      monthly.saveAll(batch);
    }
  }
}
