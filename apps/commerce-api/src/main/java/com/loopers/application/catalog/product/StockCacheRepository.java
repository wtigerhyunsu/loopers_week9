package com.loopers.application.catalog.product;

public interface StockCacheRepository {

  void init(Long productId, Long quantity);
  Long get(Long productId);
  void decrease(Long productId, Long quantity);
}
