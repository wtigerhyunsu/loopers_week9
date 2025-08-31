package com.loopers.domain.coupon.issued;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "issued_coupon")
public class IssuedCoupon extends BaseEntity {
  // 누가
  private String userId;
  // 어디에서
  private Long orderId;
  // 무엇을
  private Long couponId;
  // 사용 여부
  private boolean used;

  @Column(columnDefinition = "TEXT")
  private String memo;

  public IssuedCoupon(String userId, Long orderId, Long couponId) {
    this.userId = userId;
    this.orderId = orderId;
    this.couponId = couponId;
    this.used = true;
  }

  public IssuedCoupon(String userId, Long orderId, Long couponId, String memo) {
    this.userId = userId;
    this.orderId = orderId;
    this.couponId = couponId;
    this.used = false;
    this.memo = memo;
  }

  public void used() {
    this.used = true;
  }
}
