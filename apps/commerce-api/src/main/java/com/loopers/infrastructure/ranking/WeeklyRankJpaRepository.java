package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.WeeklyRankId;
import com.loopers.domain.ranking.WeeklyRankModel;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyRankJpaRepository extends JpaRepository<WeeklyRankModel, WeeklyRankId> {
  List<WeeklyRankModel> findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(LocalDate periodStart, LocalDate periodEnd, Pageable pageable);
}

