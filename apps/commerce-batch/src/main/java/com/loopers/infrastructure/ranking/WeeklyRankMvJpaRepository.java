package com.loopers.infrastructure.ranking;

import com.loopers.domain.ranking.WeeklyRankEntity;
import com.loopers.domain.ranking.WeeklyRankId;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeeklyRankMvJpaRepository extends JpaRepository<WeeklyRankEntity, WeeklyRankId> {
  @Modifying
  @Query("delete from WeeklyRankEntity e where e.id.periodStart = :s and e.periodEnd = :e")
  void deletePeriod(@Param("s") LocalDate start, @Param("e") LocalDate end);
}

