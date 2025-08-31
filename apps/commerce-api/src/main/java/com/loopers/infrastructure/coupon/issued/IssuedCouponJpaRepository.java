package com.loopers.infrastructure.coupon.issued;

import com.loopers.domain.coupon.issued.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCoupon, Long> {

  @Modifying
  @Query("""
      DELETE FROM IssuedCoupon IS
      WHERE IS.userId = :userId and IS.orderId = :orderId and IS.couponId = :couponId
      """)
  void deleteByOrderIdAndCouponId(@Param("userId") String userId,
                                  @Param("orderId") Long orderId,
                                  @Param("couponId") Long couponId);


  boolean existsByUserIdAndOrderIdAndCouponId(@Param("userId") String userId,
                                     @Param("orderId") Long orderId,
                                     @Param("couponId") Long couponId);
}
