package com.loopers.interfaces.api.order;

import java.math.BigInteger;
import java.util.List;

public class OrderV1Dto {
  class Create {
    record Response(
        String userId,
        Long orderId,
        String orderNumber,
        BigInteger totalPrice,
        List<OrderItemResponse> items
    ) {

    }


    record Request(
        List<OrderItemRequest> items
    ) {
    }

    record OrderItemRequest(
        Long productId, Integer quantity
    ) {
    }
  }

  class Search {
    record Response(List<Contents> contents, int page,
                    int size,
                    int totalElements,
                    int totalPages) {
    }

    record Contents(Long OrderId, String orderNumber) {
    }

  }

  class Get {
    record Response(Long orderId, String orderNumber, List<OrderItemResponse> items) {
    }
  }

  record OrderItemResponse(
      Long productId, String productName, Integer quantity
  ) {
  }
}
