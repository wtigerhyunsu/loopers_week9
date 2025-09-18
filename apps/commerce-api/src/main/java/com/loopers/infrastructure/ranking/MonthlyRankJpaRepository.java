package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.MonthlyRankId;
import com.loopers.domain.ranking.MonthlyRankModel;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyRankJpaRepository extends JpaRepository<MonthlyRankModel, MonthlyRankId> {
  List<MonthlyRankModel> findByIdPeriodStartAndPeriodEndOrderByRankAscIdProductIdAsc(LocalDate periodStart, LocalDate periodEnd, Pageable pageable);
}

