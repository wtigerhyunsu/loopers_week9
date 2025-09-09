package com.loopers.domain.metrics;

public record ViewsMetricsMessage(
    Long productId,
    int value
) {
}
