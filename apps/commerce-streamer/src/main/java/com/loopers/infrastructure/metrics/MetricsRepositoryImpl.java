package com.loopers.infrastructure.metrics;

import com.loopers.domain.metrics.MetricsModel;
import com.loopers.domain.metrics.MetricsRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MetricsRepositoryImpl implements MetricsRepository {
  private final MetricsJpaRepository metricsJpaRepository;

  public MetricsRepositoryImpl(MetricsJpaRepository metricsJpaRepository) {
    this.metricsJpaRepository = metricsJpaRepository;
  }

  @Transactional
  public void upsertLikes(Long productId, long value) {
    LocalDate now = LocalDate.now();
    Optional<MetricsModel> metricsModel = metricsJpaRepository.findByProductId(productId)
        .stream().filter(p -> p.getDate().equals(now)).findFirst();

    if (metricsModel.isEmpty()) {
      metricsJpaRepository.save(new MetricsModel(productId, 0L, value, 0L, now));
      return;
    }

    MetricsModel metrics = metricsModel.get();
    metrics.updateLikes(value);

  }

  @Transactional
  public void upsertViews(Long productId, long value) {
    LocalDate now = LocalDate.now();
    Optional<MetricsModel> metricsModel = metricsJpaRepository.findByProductId(productId)
        .stream().filter(p -> p.getDate().equals(now)).findFirst();

    if (metricsModel.isEmpty()) {
      metricsJpaRepository.save(new MetricsModel(productId, value, 0L, 0L, LocalDate.now()));
      return;
    }

    MetricsModel metrics = metricsModel.get();
    metrics.updateViews();
  }

  @Transactional
  public void upsertSales(Long productId, long value) {
    LocalDate now = LocalDate.now();
    Optional<MetricsModel> metricsModel = metricsJpaRepository.findByProductId(productId)
        .stream().filter(p -> p.getDate().equals(now)).findFirst();
    if (metricsModel.isEmpty()) {
      metricsJpaRepository.save(new MetricsModel(productId, 0L, 0L, value, LocalDate.now()));
      return;
    }

    MetricsModel metrics = metricsModel.get();
    metrics.updateSales(value);

  }

}
