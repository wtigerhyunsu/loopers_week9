package com.loopers.application.ranking;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.infrastructure.ranking.MonthlyRankJpaRepository;
import com.loopers.infrastructure.ranking.WeeklyRankJpaRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

class RankingQueryServiceTest {

  @Test
  @DisplayName("날짜가 null일 경우 마지막으로 종료된 주간을 사용한다")
  void should_use_last_closed_week_when_date_is_null() {
    WeeklyRankJpaRepository weekly = Mockito.mock(WeeklyRankJpaRepository.class);
    MonthlyRankJpaRepository monthly = Mockito.mock(MonthlyRankJpaRepository.class);
    ProductRepository products = Mockito.mock(ProductRepository.class);
    when(products.getIn(any())).thenReturn(List.of());
    when(weekly.findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(any(), any(), any()))
        .thenReturn(List.of());

    RankingQueryService svc = new RankingQueryService(weekly, monthly, products);

    LocalDate end = LocalDate.now(ZoneId.of("Asia/Seoul")).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
    LocalDate start = end.minusDays(6);

    svc.topWeekly(10, null);

    verify(weekly).findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(start, end, PageRequest.of(0, 10));
  }

  @Test
  @DisplayName("날짜가 주어지면 해당 주간의 월~일 범위를 사용한다")
  void should_use_week_boundaries_when_date_is_given() {
    WeeklyRankJpaRepository weekly = Mockito.mock(WeeklyRankJpaRepository.class);
    MonthlyRankJpaRepository monthly = Mockito.mock(MonthlyRankJpaRepository.class);
    ProductRepository products = Mockito.mock(ProductRepository.class);
    when(products.getIn(any())).thenReturn(List.of());
    when(weekly.findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(any(), any(), any()))
        .thenReturn(List.of());

    RankingQueryService svc = new RankingQueryService(weekly, monthly, products);

    LocalDate date = LocalDate.of(2024, 9, 18);
    LocalDate start = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate end = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    svc.topWeekly(7, date);

    verify(weekly).findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(start, end, PageRequest.of(0, 7));
  }

  @Test
  @DisplayName("날짜가 null일 경우 마지막으로 종료된 월간을 사용한다")
  void should_use_last_closed_month_when_date_is_null() {
    WeeklyRankJpaRepository weekly = Mockito.mock(WeeklyRankJpaRepository.class);
    MonthlyRankJpaRepository monthly = Mockito.mock(MonthlyRankJpaRepository.class);
    ProductRepository products = Mockito.mock(ProductRepository.class);
    when(products.getIn(any())).thenReturn(List.of());
    when(monthly.findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(any(), any(), any()))
        .thenReturn(List.of());

    RankingQueryService svc = new RankingQueryService(weekly, monthly, products);

    LocalDate base = LocalDate.now(ZoneId.of("Asia/Seoul")).withDayOfMonth(1).minusDays(1);
    LocalDate start = base.withDayOfMonth(1);
    LocalDate end = base.withDayOfMonth(base.lengthOfMonth());

    svc.topMonthly(5, null);

    verify(monthly).findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(start, end, PageRequest.of(0, 5));
  }

  @Test
  @DisplayName("조회 개수(limit)는 최대 100개로 제한된다")
  void should_clamp_limit_to_100() {
    WeeklyRankJpaRepository weekly = Mockito.mock(WeeklyRankJpaRepository.class);
    MonthlyRankJpaRepository monthly = Mockito.mock(MonthlyRankJpaRepository.class);
    ProductRepository products = Mockito.mock(ProductRepository.class);
    when(products.getIn(any())).thenReturn(List.of());
    when(weekly.findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(any(), any(), any()))
        .thenReturn(List.of());

    RankingQueryService svc = new RankingQueryService(weekly, monthly, products);

    var kst = ZoneId.of("Asia/Seoul");
    LocalDate end = LocalDate.now(kst).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
    LocalDate start = end.minusDays(6);

    svc.topWeekly(1000, null);

    verify(weekly).findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(start, end, PageRequest.of(0, 100));
  }

  @Test
  @DisplayName("랭킹에 매핑된 상품 정보가 응답 아이템으로 변환된다")
  void should_map_products_into_response_items() {
    WeeklyRankJpaRepository weekly = Mockito.mock(WeeklyRankJpaRepository.class);
    MonthlyRankJpaRepository monthly = Mockito.mock(MonthlyRankJpaRepository.class);
    ProductRepository products = Mockito.mock(ProductRepository.class);

    var id = new com.loopers.domain.ranking.WeeklyRankId(LocalDate.of(2024, 9, 16), 10L);
    var model = new com.loopers.domain.ranking.WeeklyRankModel(id, LocalDate.of(2024, 9, 22), 123L, 1, java.time.LocalDateTime.now());
    when(weekly.findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(any(), any(), any()))
        .thenReturn(List.of(model));

    com.loopers.domain.catalog.product.ProductModel p = new com.loopers.domain.catalog.product.ProductModel(1L, "p10", java.math.BigInteger.valueOf(1000), null);
    when(products.getIn(any())).thenReturn(List.of(p));

    RankingQueryService svc = new RankingQueryService(weekly, monthly, products);
    var res = svc.topWeekly(10, LocalDate.of(2024, 9, 18));

    org.assertj.core.api.Assertions.assertThat(res.items()).hasSize(1);
    var item = res.items().get(0);
    org.assertj.core.api.Assertions.assertThat(item.productId()).isEqualTo(10L);
    org.assertj.core.api.Assertions.assertThat(item.rank()).isEqualTo(1);
    org.assertj.core.api.Assertions.assertThat(item.name()).isEqualTo("p10");
  }
}
