package com.loopers.domain.catalog.product.stock;

public interface StockRepository {
  StockModel get(Long productId);
}
