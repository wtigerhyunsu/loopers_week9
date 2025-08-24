package com.loopers.application.payment;

public record StockDecreaseCommand(Long productId, Long quantity) {
}
