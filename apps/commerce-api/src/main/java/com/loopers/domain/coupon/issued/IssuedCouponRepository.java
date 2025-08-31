package com.loopers.domain.coupon.issued;

public interface IssuedCouponRepository {
  void register(IssuedCoupon issuedCoupon);

  boolean exists(String userId, Long orderId, Long couponId);

  void removeCoupon(String userId, Long orderId, Long couponId);
}
