package com.loopers.interfaces.api.order;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderItemResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.Search;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderV1Controller {


  @PostMapping
  public ApiResponse<OrderV1Dto.Create.Response> crete(@RequestHeader("X-USER-ID") String userId,
  OrderV1Dto.Create.Request request
  ) {

    return ApiResponse.success(new OrderV1Dto.Create.Response(
        userId,
        1L,
        UUID.randomUUID().toString(),
        BigInteger.valueOf(20000),
        List.of(
            new OrderItemResponse(1L, "상품1", 2),
            new OrderItemResponse(2L, "상품2", 5)
        )
    ));
  }

  @GetMapping
  public ApiResponse<OrderV1Dto.Search.Response> search(@RequestHeader("X-USER-ID") String userId
  ) {

    return ApiResponse.success(new OrderV1Dto.Search.Response(
        List.of(new Search.Contents(1L,"주문번호1"),
                new Search.Contents(2L,"주문번호2")),
        1,1,1,1
    ));
  }

  @GetMapping("/{orderId}")
  public ApiResponse<OrderV1Dto.Get.Response> get(@RequestHeader("X-USER-ID") String userId,
                                                  @PathVariable Long orderId
  ) {

    return ApiResponse.success(new OrderV1Dto.Get.Response(
        orderId, "주문번호1",
        List.of(
            new OrderItemResponse(1L,"상품1",3),
            new OrderItemResponse(2L,"상품2",2)
        )
    ));
  }
}
