package com.loopers.application.order.info;

import com.loopers.domain.order.OrderModel;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;

@Builder(builderMethodName = "create")
public record OrderCreateInfo(
    String userId,
    Long orderId,
    String orderNumber,
    String orderStatus,
    String address,
    List<ItemInfos> items,
    BigInteger totalPrice,
    String memo,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt
) {
  public static OrderCreateInfo from(OrderModel orderModel) {
    return OrderCreateInfo.create()
        .userId(orderModel.getUserId())
        .orderId(orderModel.getId())
        .items(ItemInfos.from(orderModel.getOrderItems()))
        .orderNumber(orderModel.getOrderNumber())
        .orderStatus(orderModel.getStatus().name())
        .address(orderModel.getAddress())
        .totalPrice(orderModel.getTotalPrice())
        .memo(orderModel.getMemo())
        .createdAt(orderModel.getCreatedAt())
        .updatedAt(orderModel.getUpdatedAt())
        .build();
  }
}
