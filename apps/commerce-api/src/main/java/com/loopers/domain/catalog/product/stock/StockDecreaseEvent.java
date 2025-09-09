package com.loopers.domain.catalog.product.stock;

public record StockDecreaseEvent(
    Long productId,
    Long quantity,
    Long current
) {
}
