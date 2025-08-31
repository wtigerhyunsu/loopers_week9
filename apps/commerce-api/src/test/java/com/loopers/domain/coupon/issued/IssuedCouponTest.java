package com.loopers.domain.coupon.issued;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IssuedCouponTest {

  @Test
  @DisplayName("쿠폰을 최초로 생성할때는 사용여부를 false로 둡니다.")
  void crateInitCoupon() {
    // given
    IssuedCoupon issuedCoupon = new IssuedCoupon("userId", 1L, 1L, "memo");
    // when & then
    assertThat(issuedCoupon.isUsed()).isFalse();
  }

  @Test
  @DisplayName("발급된 쿠폰 사용시 사용여부 변경")
  void usedCoupon() {
    // given
    IssuedCoupon issuedCoupon = new IssuedCoupon("userId", 1L, 1L, "memo");

    // when
    issuedCoupon.used();

    // then
    assertThat(issuedCoupon.isUsed()).isTrue();
  }


}
