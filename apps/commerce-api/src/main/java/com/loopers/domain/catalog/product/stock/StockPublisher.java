package com.loopers.domain.catalog.product.stock;

import com.loopers.support.shared.Message;

public interface StockPublisher {
  void evict(Long productId);
  void aggregate(Long productId, Long quantity);
  void aggregate(Message message, Long productId);
}
