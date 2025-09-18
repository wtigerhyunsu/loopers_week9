package com.loopers.domain.ranking;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

public class PeriodResolver {
  private final ZoneId zoneId;

  public PeriodResolver(ZoneId zoneId) {
    this.zoneId = zoneId;
  }

  public Range lastClosedWeek() {
    LocalDate end = LocalDate.now(zoneId).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
    LocalDate start = end.minusDays(6);
    return new Range(start, end);
  }

  public Range lastClosedMonth() {
    LocalDate base = LocalDate.now(zoneId).withDayOfMonth(1).minusDays(1);
    LocalDate start = base.withDayOfMonth(1);
    LocalDate end = base.withDayOfMonth(base.lengthOfMonth());
    return new Range(start, end);
  }

  public Range weekOf(LocalDate date) {
    LocalDate start = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate end = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    return new Range(start, end);
  }

  public Range monthOf(LocalDate date) {
    LocalDate start = date.withDayOfMonth(1);
    LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
    return new Range(start, end);
  }

  public record Range(LocalDate start, LocalDate end) {}
}

