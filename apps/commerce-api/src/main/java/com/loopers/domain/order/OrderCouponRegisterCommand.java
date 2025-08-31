package com.loopers.domain.order;

public record OrderCouponRegisterCommand(
    String userId,
    Long orderId,
    Long couponId
) {
}
