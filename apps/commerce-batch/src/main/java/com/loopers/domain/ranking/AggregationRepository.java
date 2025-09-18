package com.loopers.domain.ranking;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AggregationRepository {
  Map<Long, Map<LocalDate, long[]>> loadDailyMetrics(LocalDate start, LocalDate end);

  void replaceWeekly(LocalDate start, LocalDate end, List<RankEntry> entries);

  void replaceMonthly(LocalDate start, LocalDate end, List<RankEntry> entries);

  record RankEntry(long productId, long score, int rank) {}
}

