package com.loopers.application.coupon;

import com.loopers.domain.coupon.CouponRollbackCommand;
import com.loopers.domain.coupon.issued.IssuedCouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRollbackListener {
  private final IssuedCouponRepository issuedCouponRepository;
  private final CouponRollbackHandler couponRollbackHandler;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void rollback(CouponRollbackCommand command) {
    if (!issuedCouponRepository.exists(command.userId(), command.orderId(), command.couponId())) {
      log.warn("이미 처리된 쿠폰 사용 롤백 이벤트입니다. Order ID: {}, Coupon ID: {}", command.orderId(), command.couponId());
      return;
    }
    couponRollbackHandler.rollback(command);
  }


}
