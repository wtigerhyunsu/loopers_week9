package com.loopers.domain.metrics;

public interface MetricsRepository {
  void upsertLikes(Long productId, long value);
  void upsertViews(Long productId, long value);
  void upsertSales(Long productId, long value);
}
