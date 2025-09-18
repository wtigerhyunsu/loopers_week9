package com.loopers.infrastructure.metrics;

import com.loopers.domain.metrics.ProductMetricsModel;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductMetricsJpaRepository extends JpaRepository<ProductMetricsModel, Long> {
  List<ProductMetricsModel> findByDateBetween(LocalDate start, LocalDate end);

  @Query("select distinct m.productId from ProductMetricsModel m where m.date between :start and :end order by m.productId asc")
  List<Long> findDistinctProductIdsByDateBetween(@Param("start") LocalDate start,
                                                 @Param("end") LocalDate end,
                                                 Pageable pageable);

  List<ProductMetricsModel> findByProductIdAndDateBetween(Long productId, LocalDate start, LocalDate end);
}
