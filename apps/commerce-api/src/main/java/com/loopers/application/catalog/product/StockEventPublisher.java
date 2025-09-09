package com.loopers.application.catalog.product;

public interface StockEventPublisher {
  void decrease(Long productId, Long quantity, Long current);
}
