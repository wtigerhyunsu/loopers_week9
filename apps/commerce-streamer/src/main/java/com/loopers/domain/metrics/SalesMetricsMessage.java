package com.loopers.domain.metrics;

public record SalesMetricsMessage(
    Long productId,
    Integer quantity
) {
}
