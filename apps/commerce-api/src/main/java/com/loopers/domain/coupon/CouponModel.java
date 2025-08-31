package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigInteger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "coupon")
public class CouponModel extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private CouponMethod method;


  private int count;

  private int discountValue;

  @Column(columnDefinition = "TEXT")
  private String description;

  public CouponModel(String method, int count, int discountValue, String description) {
    validate(count, discountValue);
    this.method = CouponMethod.convent(method);
    this.count = count;
    this.discountValue = discountValue;
    this.description = description;
  }

  public void validate(int count, int discountValue) {
    if (count <= 0) {
      throw new IllegalArgumentException("쿠폰은 0개 이하로 생성할 수 없습니다.");
    }

    if (discountValue <= 0) {
      throw new IllegalArgumentException("할인 수치는 0 이하로 작성할 수 없습니다.");
    }
  }

  public void issued() {
    this.count -= 1;
    if (count < 0) {
      throw new IllegalArgumentException("더이상 쿠폰을 발급 할 수 없습니다.");
    }
  }

  public BigInteger calculate(BigInteger price) {
    return switch (method) {
      case FIXED -> price.subtract(BigInteger.valueOf(discountValue));
      case PERCENT -> price.multiply(BigInteger.valueOf(100 - discountValue)).divide(BigInteger.valueOf(100));
    };
  }

  public void used() {
    this.count -= 1;
  }
}

enum CouponMethod {
  PERCENT, FIXED;

  public static CouponMethod convent(String coupon) {
    return switch (coupon) {
      case "PERCENT" -> PERCENT;
      case "FIXED" -> FIXED;
      default -> throw new IllegalArgumentException("존재하지 않는 쿠폰 타입입니다.");
    };
  }
}
