package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCreateCommand;
import com.loopers.application.order.OrderCreateInfo;
import com.loopers.application.order.OrderItemCommands;
import java.math.BigInteger;
import java.util.List;

public class OrderV1Dto {
  public class Create {
    public record Response(
        String userId,
        Long orderId,
        String orderNumber,
        BigInteger totalPrice,
        BigInteger discountPrice,
        List<OrderItemResponse> items
    ) {

      public Response(OrderCreateInfo orderCreateInfo) {
        this(orderCreateInfo.userId(),
            orderCreateInfo.orderId(),
            orderCreateInfo.orderNumber(),
            orderCreateInfo.totalPrice(),
            orderCreateInfo.discountPrice(),
            orderCreateInfo.items().stream().map(o -> new OrderItemResponse(o.productId(), o.quantity(), o.unitPrice())).toList()
        );
      }
    }


    public record Request(
        String address,
        List<OrderItemRequest> items,
        Long couponId,
        String memo
    ) {
      public OrderCreateCommand toCommand(String userId) {
        return new OrderCreateCommand(userId,
            address,
            items.stream().map(o -> new OrderItemCommands(o.productId, o.quantity)).toList(),
            couponId,
            memo);
      }
    }

    record OrderItemRequest(
        Long productId, Long quantity
    ) {
    }
  }

  public class Search {
    public record Response(List<Contents> contents, int page,
                           int size,
                           int totalElements,
                           int totalPages) {
    }

    public record Contents(Long OrderId, String orderNumber) {
    }

  }

  public class Get {
    public record Response(Long orderId, String orderNumber, List<OrderItemResponse> items) {
    }
  }

  public record OrderItemResponse(
      Long productId, Long quantity, BigInteger unitPrice
  ) {
  }
}
