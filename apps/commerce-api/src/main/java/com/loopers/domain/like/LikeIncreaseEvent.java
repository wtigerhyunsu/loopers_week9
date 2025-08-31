package com.loopers.domain.like;

public record LikeIncreaseEvent(
    String userId,
    Long productId
) {
}
