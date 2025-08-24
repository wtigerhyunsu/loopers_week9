package com.loopers.application.order;

import com.loopers.domain.order.orderItem.OrderItemModel;
import java.math.BigInteger;
import java.util.List;

public record ItemInfos(
    Long productId,
    Long quantity,
    BigInteger unitPrice
) {

  public static List<ItemInfos> from(List<OrderItemModel> orderItems) {
    return orderItems
        .stream().map(
            orderItem -> new ItemInfos(
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice()
            )
        ).toList();
  }
}
