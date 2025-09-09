package com.loopers.infrastructure.catalog.product.stock;

import com.loopers.application.catalog.product.StockCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockRedisCacheRepository implements StockCacheRepository {

  private final RedisTemplate<String, String> redisTemplate;
  private final static String KEY = "stock:product:";


  @Override
  public void init(Long productId, Long quantity) {
    redisTemplate.opsForValue().set(KEY + productId, String.valueOf(quantity));
  }

  @Override
  public Long get(Long productId) {
    String stockValue = redisTemplate.opsForValue().get(KEY + productId);

    if(stockValue == null) {
      return 0L;
    }

    return Long.valueOf(stockValue);
  }

  @Override
  public void decrease(Long productId, Long quantity) {
    redisTemplate.opsForValue().decrement(KEY + productId, quantity);
  }

}
