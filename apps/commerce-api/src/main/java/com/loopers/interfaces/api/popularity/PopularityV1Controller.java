package com.loopers.interfaces.api.popularity;

import com.loopers.application.popularity.PopularityService;
import com.loopers.interfaces.api.ApiResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import com.loopers.application.ranking.RankingQueryService;
import java.time.ZoneId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/popularity")
@RequiredArgsConstructor
public class PopularityV1Controller {
  private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
  private final PopularityService popularityService;
  private final RankingQueryService rankingQueryService;

  @GetMapping("/top")
  public ApiResponse<PopularityResponse.TopRanking> top(
      @RequestParam(name = "limit", defaultValue = "10") int limit,
      @RequestParam(name = "date", required = false) String date,
      @RequestParam(name = "period", required = false, defaultValue = "daily") String period
  ) {
    if (limit < 1 || limit > 100) throw new IllegalArgumentException("limit out of range");
    LocalDate d = date == null ? null : LocalDate.parse(date, DATE);
    String scope = period == null ? "daily" : period.toLowerCase();
    if ("weekly".equals(scope)) {
      return ApiResponse.success(rankingQueryService.topWeekly(limit, d));
    } else if ("monthly".equals(scope)) {
      return ApiResponse.success(rankingQueryService.topMonthly(limit, d));
    }
    LocalDate dailyDate = d == null ? LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1) : d;
    return ApiResponse.success(popularityService.topN(limit, dailyDate));
  }

  @GetMapping("/{productId}")
  public ApiResponse<PopularityResponse.ProductRank> rank(
      @PathVariable("productId") long productId,
      @RequestParam(name = "date", required = false) String date
  ) {
    LocalDate d = date == null ? null : LocalDate.parse(date, DATE);
    return ApiResponse.success(popularityService.rankOf(productId, d));
  }
}
