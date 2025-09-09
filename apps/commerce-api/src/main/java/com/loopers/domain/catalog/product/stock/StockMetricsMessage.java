package com.loopers.domain.catalog.product.stock;

public record StockMetricsMessage(
    Long productId,
    Long quantity
) {
}
