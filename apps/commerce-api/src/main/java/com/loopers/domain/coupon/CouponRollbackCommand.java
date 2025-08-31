package com.loopers.domain.coupon;

public record CouponRollbackCommand(
    String userId,
    Long orderId,
    Long couponId
) {
}
