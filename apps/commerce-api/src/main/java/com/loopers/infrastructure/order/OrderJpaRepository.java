package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OrderJpaRepository extends JpaRepository<OrderModel, Long> {
  Optional<OrderModel> findById(Long id);

  @Query("SELECT o FROM OrderModel o WHERE o.orderNumber.number = :orderNumber")
  Optional<OrderModel> findByOrderNumber(String orderNumber);

  @Query("SELECT o FROM OrderModel o WHERE o.userId = :userId and o.orderNumber.number = :orderNumber")
  Optional<OrderModel> findByUserIdAndOrderNumber(String userId, String orderNumber);

  @Modifying
  @Query("""
        UPDATE OrderModel o
           SET o.discountPrice = 0
          WHERE o.id = :orderId
      """)
  void clearDiscount(Long orderId);
}
