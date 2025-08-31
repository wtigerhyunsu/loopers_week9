package com.loopers.infrastructure.coupon.issued;

import com.loopers.domain.coupon.issued.IssuedCoupon;
import com.loopers.domain.coupon.issued.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IssuedCouponRepositoryImpl implements IssuedCouponRepository {
  private final IssuedCouponJpaRepository repository;

  @Override
  public void register(IssuedCoupon issuedCoupon) {
    repository.save(issuedCoupon);
  }

  @Override
  public boolean exists(String userId, Long orderId, Long couponId) {
    return repository.existsByUserIdAndOrderIdAndCouponId(userId, orderId, couponId);
  }

  @Override
  public void removeCoupon(String userId, Long orderId, Long couponId) {
    repository.deleteByOrderIdAndCouponId(userId, orderId, couponId);
  }

}
