package com.loopers.domain.catalog.product.cache;

import java.time.Duration;

public interface ProductCacheRepository {
  void put(String key, String value, Duration duration);

  String get(String key);

  void remove(String key);
}
