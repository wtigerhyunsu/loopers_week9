package com.loopers.application.order;

public record OrderItemCommands(
    Long productId,
    Long quantity
) {
}
