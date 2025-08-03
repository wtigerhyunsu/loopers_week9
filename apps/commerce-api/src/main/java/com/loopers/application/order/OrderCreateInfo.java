package com.loopers.application.order;

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
}

record ItemInfos(
    Long productId,
    Long quantity,
    BigInteger unitPrice
) {

}
