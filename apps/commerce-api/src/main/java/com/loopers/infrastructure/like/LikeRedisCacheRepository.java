package com.loopers.infrastructure.like;

import com.loopers.application.like.LikeCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeRedisCacheRepository implements LikeCacheRepository {
  private final RedisTemplate<String, String> redisTemplate;
  private final static String KEY = "like:product:";

  public void increase(Long productId) {
    redisTemplate.opsForValue().increment(KEY + productId);
  }

  public void decrement(Long productId) {
    redisTemplate.opsForValue().decrement(KEY + productId);
  }
}
