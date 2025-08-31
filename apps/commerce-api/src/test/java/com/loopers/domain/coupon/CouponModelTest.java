package com.loopers.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CouponModelTest {

  @Test
  @DisplayName("쿠폰 메소드 타입이 FIXED인 경우 할인 금액 계산")
  void returnDiscountPrice_whenCouponMethodIsFixed() {
    // given
    int count = 1;
    CouponMethod method = CouponMethod.FIXED;
    CouponModel coupon = new CouponModel(method.name(), count, 1000, "-1000원 할인 쿠폰");

    // when
    BigInteger result = coupon.calculate(BigInteger.valueOf(2000));

    assertThat(result).isEqualTo(BigInteger.valueOf(1000));
  }

  @Test
  @DisplayName("쿠폰 메소드 타입이 PERCENT인 경우 할인 금액 계산")
  void returnDiscountPrice_whenCouponMethodIsPERCENT() {
    // given
    int count = 1;
    CouponMethod method = CouponMethod.PERCENT;
    CouponModel coupon = new CouponModel(method.name(), count, 10, "10% 할인 쿠폰");

    // when
    BigInteger result = coupon.calculate(BigInteger.valueOf(1000));

    assertThat(result).isEqualTo(BigInteger.valueOf(900));
  }


  @Test
  @DisplayName("쿠폰 생성 테스트")
  void createCoupon() {
    // given
    String method = "PERCENT";
    int count = 10;
    int discountValue = 1000;
    String description = "10% 할인 쿠폰";

    // when
    CouponModel coupon = new CouponModel(method, count, discountValue, description);

    // then
    assertThat(coupon).isNotNull();
    assertThat(coupon.getMethod()).isEqualTo(CouponMethod.PERCENT);
    assertThat(coupon.getCount()).isEqualTo(count);
    assertThat(coupon.getDiscountValue()).isEqualTo(discountValue);
    assertThat(coupon.getDescription()).isEqualTo(description);
  }

  @Test
  @DisplayName("쿠폰 생성 시 수량이 0 이하일 경우 예외 발생")
  void createCouponWithInvalidCount() {
    // given
    String method = "PERCENT";
    int count = 0;
    int discountValue = 1000;
    String description = "10% 할인 쿠폰";

    // when & then
    Exception result = assertThrows(IllegalArgumentException.class, () -> new CouponModel(method, count, discountValue, description));
  }

  @Test
  @DisplayName("쿠폰 생성 시 할인 수치가 0 이하일 경우 예외 발생")
  void createCouponWithInvalidDiscountValue() {
    // given
    String method = "PERCENT";
    int count = 10;
    int discountValue = 0;
    String description = "10% 할인 쿠폰";

    // when & then
    Exception result = assertThrows(IllegalArgumentException.class, () -> new CouponModel(method, count, discountValue, description));

  }

  @Test
  @DisplayName("쿠폰 발급 시 수량 감소시 수량이 0 이하일 경우 예외 발생")
  void throwException_whenCountIsZero() {
    // given
    int count = 1;
    CouponModel coupon = new CouponModel("PERCENT", count, 1000, "10% 할인 쿠폰");

    // 1차 발급
    coupon.issued();

    // when & then
    Exception result = assertThrows(IllegalArgumentException.class, coupon::issued);


  }


  @Test
  @DisplayName("쿠폰 발급 시 수량 감소")
  void issuedCoupon() {
    // given
    int count = 10;
    CouponModel coupon = new CouponModel("PERCENT", count, 1000, "10% 할인 쿠폰");

    // when
    coupon.issued();

    assertThat(coupon.getCount()).isEqualTo((count - 1));
  }

}
