package com.loopers.domain.coupon;

public interface CouponRepository {
  CouponModel get(Long couponId);
  void used(Long couponId);

  void unUsed(Long couponId);
}
