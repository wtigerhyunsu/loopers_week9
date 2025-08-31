package com.loopers.application.coupon;

public interface CouponPublisher {
  void rollback(String userId, Long orderId, Long couponId);
}
