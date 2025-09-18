package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.MonthlyRankEntity;
import com.loopers.domain.ranking.MonthlyRankId;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MonthlyRankMvJpaRepository extends JpaRepository<MonthlyRankEntity, MonthlyRankId> {
  @Modifying
  @Query("delete from MonthlyRankEntity e where e.id.periodStart = :s and e.periodEnd = :e")
  void deletePeriod(@Param("s") LocalDate start, @Param("e") LocalDate end);
}

