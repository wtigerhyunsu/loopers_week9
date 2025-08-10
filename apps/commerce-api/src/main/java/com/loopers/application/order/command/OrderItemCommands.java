package com.loopers.application.order.command;

public record OrderItemCommands(
    Long productId,
    Long quantity
) {
}
