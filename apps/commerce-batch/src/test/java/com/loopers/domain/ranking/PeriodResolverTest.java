package com.loopers.domain.ranking;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PeriodResolverTest {

    @Test
    @DisplayName("마지막으로 종료된 주간은 이전 일요일에 끝나야 한다")
    void last_closed_week_should_end_on_previous_sunday() {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        PeriodResolver r = new PeriodResolver(kst);
        LocalDate end = LocalDate.now(kst).with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        LocalDate start = end.minusDays(6);
        var range = r.lastClosedWeek();
        assertThat(range.start()).isEqualTo(start);
        assertThat(range.end()).isEqualTo(end);
    }

    @Test
    @DisplayName("마지막으로 종료된 월간은 지난달 전체 기간을 포함해야 한다")
    void last_closed_month_should_cover_last_month() {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        PeriodResolver r = new PeriodResolver(kst);
        LocalDate base = LocalDate.now(kst).withDayOfMonth(1).minusDays(1);
        LocalDate start = base.withDayOfMonth(1);
        LocalDate end = base.withDayOfMonth(base.lengthOfMonth());
        var range = r.lastClosedMonth();
        assertThat(range.start()).isEqualTo(start);
        assertThat(range.end()).isEqualTo(end);
    }
}

