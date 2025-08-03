package com.loopers.domain.order;

public interface OrderRepository {
  OrderModel save(OrderModel orderModel);

  OrderModel ofOrderNumber(String orderNumber);

  OrderModel ofOrderNumber(String userId, String orderNumber);
}
