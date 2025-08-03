package com.loopers.infrastructure.order.history;

import com.loopers.domain.order.history.OrderHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryJpaRepository extends JpaRepository<OrderHistoryModel, Long> {
}
