package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.CouponModel;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
  private final CouponJpaRepository repository;

  @Override
  public CouponModel get(Long couponId) {
    return repository.findById(couponId)
        .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당하는 쿠폰은 존재하지 않습니다."));
  }

  @Override
  public void used(Long couponId) {
    repository.decreaseCouponCount(couponId);
  }

  @Override
  public void unUsed(Long couponId) {
    repository.increaseCouponCount(couponId);
  }

}
