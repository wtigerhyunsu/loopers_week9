package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.RankRow;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// reads from mv_product_rank_monthly
public interface MonthlyRankReadRepository {
  List<RankRow> findTop(LocalDate date, int limit, int offset);
  Optional<RankRow> rankOf(LocalDate date, long productId);
}

