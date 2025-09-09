package com.loopers.domain.catalog.product.stock;

public record StockEvictMessage(
    Long productId,
    String key

) {
  public StockEvictMessage(Long productId) {
    this(productId, "stock:product:" + productId);
  }
}
