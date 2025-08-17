package com.loopers.infrastructure.catalog.product.cache;

import com.loopers.domain.catalog.product.cache.ProductCacheRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductCacheRankRepositoryImpl implements ProductCacheRepository {
  private final RedisTemplate<String, String> redisTemplate;


  @Override
  public void put(String key, String value, Duration duration) {
    redisTemplate.opsForValue().set(key, value, duration);
  }

  @Override
  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void remove(String key) {
    redisTemplate.delete(key);
  }
}
