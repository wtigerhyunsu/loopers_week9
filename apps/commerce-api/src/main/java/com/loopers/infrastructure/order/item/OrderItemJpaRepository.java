package com.loopers.infrastructure.order.item;

import com.loopers.domain.order.orderItem.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemJpaRepository extends JpaRepository<OrderItemModel, Long> {
}
