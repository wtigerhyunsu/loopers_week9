package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.CouponModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponJpaRepository extends JpaRepository<CouponModel, Long> {

  @Modifying
  @Query("""
        UPDATE CouponModel c
        SET c.count = c.count - 1
        WHERE c.id = :couponId
        """)
  void decreaseCouponCount(@Param("couponId") Long couponId);

  @Modifying
  @Query("""
      UPDATE CouponModel c
      SET c.count = c.count + 1
      WHERE c.id = :couponId
      """)
  void increaseCouponCount(@Param("couponId")Long couponId);
}
