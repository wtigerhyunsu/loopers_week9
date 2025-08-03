package com.loopers.domain.order.history;

import com.loopers.domain.order.OrderModel;

public interface OrderHistoryRepository {
  OrderHistoryModel save(OrderModel model);
}
