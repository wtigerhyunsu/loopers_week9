package com.loopers.interfaces.api.popularity;

import com.loopers.application.popularity.PopularityService;
import com.loopers.interfaces.api.ApiResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
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

  @GetMapping("/top")
  public ApiResponse<PopularityResponse.TopRanking> top(
      @RequestParam(name = "limit", defaultValue = "10") int limit,
      @RequestParam(name = "date", required = false) String date
  ) {
    if (limit < 1 || limit > 100) throw new IllegalArgumentException("limit out of range");
    LocalDate d = date == null ? null : LocalDate.parse(date, DATE);
    return ApiResponse.success(popularityService.topN(limit, d));
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
