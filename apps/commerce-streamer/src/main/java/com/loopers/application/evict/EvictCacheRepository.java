package com.loopers.application.evict;

public interface EvictCacheRepository {
  void evict(String key);
}
