package com.loopers.application.coupon;

import com.loopers.domain.coupon.CouponRepository;
import com.loopers.domain.coupon.CouponRollbackCommand;
import com.loopers.domain.coupon.issued.IssuedCouponRepository;
import com.loopers.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CouponRollbackHandler {
  private final IssuedCouponRepository issuedCouponRepository;
  private final CouponRepository couponRepository;
  private final OrderRepository orderRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void rollback(CouponRollbackCommand command) {
    // 쿠폰 count 롤백
    couponRepository.unUsed(command.couponId());
    // 쿠폰 사용 취소
    issuedCouponRepository.removeCoupon(command.userId(), command.orderId(), command.couponId());
    // 할인률 취소
    orderRepository.clearDiscount(command.orderId());
  }
}
