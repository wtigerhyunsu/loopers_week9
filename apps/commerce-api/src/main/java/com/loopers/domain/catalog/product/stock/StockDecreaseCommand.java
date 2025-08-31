package com.loopers.domain.catalog.product.stock;

import com.loopers.domain.order.orderItem.OrderItemModel;
import java.util.List;

public record StockDecreaseCommand(List<OrderItemModel> orderItems) {
}
