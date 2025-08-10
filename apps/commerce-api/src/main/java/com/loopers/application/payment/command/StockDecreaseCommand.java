package com.loopers.application.payment.command;

public record StockDecreaseCommand(Long productId, Long quantity) {
}
