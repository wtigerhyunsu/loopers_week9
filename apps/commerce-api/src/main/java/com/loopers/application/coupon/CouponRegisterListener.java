package com.loopers.application.coupon;

import com.loopers.application.order.CouponProcessor;
import com.loopers.domain.coupon.issued.IssuedCouponRepository;
import com.loopers.domain.order.OrderCouponRegisterCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponRegisterListener {
  private final IssuedCouponRepository issuedCouponRepository;
  private final CouponProcessor processor;
  private final CouponPublisher publisher;


  @TransactionalEventListener
  public void handle(OrderCouponRegisterCommand command) {
    // 멱등성 확보: 동일한 이벤트가 중복으로 발생하더라도 쿠폰이 중복으로 사용되는 것을 방지합니다.
    if (issuedCouponRepository.exists(command.userId(), command.orderId(), command.couponId())) {
      log.warn("이미 처리된 쿠폰 사용 등록 이벤트입니다. Order ID: {}, Coupon ID: {}", command.orderId(), command.couponId());
      return;
    }

    log.info("쿠폰 사용 등록을 시작합니다. Order ID: {}, Coupon ID: {}", command.orderId(), command.couponId());

    processor.register(command);
    publisher.rollback(command.userId(), command.orderId(), command.couponId());
  }

  @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
  public void rollback(OrderCouponRegisterCommand command) {
    log.info("쿠폰 사용 등록이 롤백이 되었습니다. Order ID: {}, Coupon ID: {}", command.orderId(), command.couponId());
  }


}
