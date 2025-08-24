package com.loopers.application.order;

import java.util.List;

public record OrderCreateCommand(
    String userId,
    String address,
    List<OrderItemCommands> items,
    String memo
) {
}



