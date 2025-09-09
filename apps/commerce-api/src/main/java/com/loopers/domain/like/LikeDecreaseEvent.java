package com.loopers.domain.like;

public record LikeDecreaseEvent(
    String userId,
    Long productId,
    int current
) {
}
