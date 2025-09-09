package com.loopers.infrastructure.evict;

import com.loopers.application.evict.EvictCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EvictRedisCacheRepository implements EvictCacheRepository {
  private final RedisTemplate<String, String> redisTemplate;

  public EvictRedisCacheRepository(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void evict(String key) {
    String message = key.substring(1, key.length() - 1);
    redisTemplate.delete(message);
  }
}
