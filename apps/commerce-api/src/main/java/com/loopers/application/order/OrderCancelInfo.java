package com.loopers.application.order;

import java.math.BigInteger;
import java.util.List;

public record OrderCancelInfo(
    String userId,
    String orderNumber,
    List<CancelItems> items,
    String orderStaus
) {
}

record CancelItems(
    Long productId,
    Long quantity,
    BigInteger price
) {
}
