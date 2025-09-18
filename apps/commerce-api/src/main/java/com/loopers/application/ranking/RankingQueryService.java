package com.loopers.application.ranking;

import com.loopers.domain.catalog.product.ProductModel;
import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.domain.ranking.MonthlyRankModel;
import com.loopers.domain.ranking.WeeklyRankModel;
import com.loopers.interfaces.api.popularity.PopularityResponse;
import com.loopers.infrastructure.ranking.MonthlyRankJpaRepository;
import com.loopers.infrastructure.ranking.WeeklyRankJpaRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RankingQueryService {
  private static final ZoneId KST = ZoneId.of("Asia/Seoul");
  private final WeeklyRankJpaRepository weeklyRepo;
  private final MonthlyRankJpaRepository monthlyRepo;
  private final ProductRepository productRepository;

  public RankingQueryService(WeeklyRankJpaRepository weeklyRepo, MonthlyRankJpaRepository monthlyRepo, ProductRepository productRepository) {
    this.weeklyRepo = weeklyRepo;
    this.monthlyRepo = monthlyRepo;
    this.productRepository = productRepository;
  }

  public PopularityResponse.TopRanking topWeekly(int limit, LocalDate date) {
    LocalDate start;
    LocalDate end;
    if (date == null) {
      end = LocalDate.now(KST).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
      start = end.minusDays(6);
    } else {
      start = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
      end = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }
    List<WeeklyRankModel> rows = weeklyRepo.findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(start, end, PageRequest.of(0, clamp(limit)));
    return toResponse(rows);
  }

  public PopularityResponse.TopRanking topMonthly(int limit, LocalDate date) {
    LocalDate base = date != null ? date : LocalDate.now(KST).withDayOfMonth(1).minusDays(1);
    LocalDate start = base.withDayOfMonth(1);
    LocalDate end = base.withDayOfMonth(base.lengthOfMonth());
    List<MonthlyRankModel> rows = monthlyRepo.findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(start, end, PageRequest.of(0, clamp(limit)));
    return toResponse(rows);
  }

  private PopularityResponse.TopRanking toResponse(List<?> rows) {
    List<Long> productIds = new ArrayList<>();
    for (Object o : rows) {
      if (o instanceof WeeklyRankModel w) productIds.add(w.getId().getProductId());
      else if (o instanceof MonthlyRankModel m) productIds.add(m.getId().getProductId());
    }
    Map<Long, ProductModel> productMap = productRepository.getIn(productIds).stream()
        .collect(Collectors.toMap(ProductModel::getId, Function.identity()));

    List<PopularityResponse.RankingItem> items = new ArrayList<>();
    for (Object o : rows) {
      long productId;
      int rank;
      if (o instanceof WeeklyRankModel w) {
        productId = w.getId().getProductId();
        rank = w.getRank();
      } else {
        MonthlyRankModel m = (MonthlyRankModel) o;
        productId = m.getId().getProductId();
        rank = m.getRank();
      }
      ProductModel p = productMap.get(productId);
      if (p == null) continue;
      items.add(new PopularityResponse.RankingItem(productId, p.getName(), p.getPrice(), null, rank));
    }
    return new PopularityResponse.TopRanking(items);
  }

  private int clamp(int limit) {
    return Math.max(1, Math.min(limit, 100));
  }
}
