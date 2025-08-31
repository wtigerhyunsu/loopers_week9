package com.loopers.infrastructure.coupon;

import com.loopers.application.coupon.CouponPublisher;
import com.loopers.domain.coupon.CouponRollbackCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponCorePublisher implements CouponPublisher {
  private final ApplicationEventPublisher publisher;

  public void rollback(String userId, Long orderId, Long couponId) {
    publisher.publishEvent(new CouponRollbackCommand(userId, orderId, couponId));
  }
}
