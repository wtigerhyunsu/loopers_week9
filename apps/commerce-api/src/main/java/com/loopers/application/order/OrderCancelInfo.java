package com.loopers.application.order;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.orderItem.OrderItemModel;
import java.math.BigInteger;
import java.util.List;

public record OrderCancelInfo(
    String userId,
    String orderNumber,
    List<CancelItems> items,
    String orderStaus
) {
  public static OrderCancelInfo from(OrderModel orderModel) {
    return new OrderCancelInfo(
        orderModel.getUserId(),
        orderModel.getOrderNumber(),
        CancelItems.from(orderModel.getOrderItems())
        , orderModel.getStatus().name());
  }

  record CancelItems(
      Long productId,
      Long quantity,
      BigInteger price
  ) {
    public static List<CancelItems> from(List<OrderItemModel> orderItems) {
      return orderItems.stream().map(item ->
          new CancelItems(
              item.getProductId(),
              item.getQuantity(),
              item.getUnitPrice())).toList();
    }
  }
}
