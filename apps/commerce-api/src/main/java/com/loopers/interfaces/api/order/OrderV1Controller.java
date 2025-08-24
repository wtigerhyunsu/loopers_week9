package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderFacade;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.OrderItemResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.Search;
import java.math.BigInteger;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderV1Controller {
  private final OrderFacade orderFacade;

  @PostMapping
  public ApiResponse<OrderV1Dto.Create.Response> create(@RequestHeader("X-USER-ID") String userId,
                                                        @RequestBody OrderV1Dto.Create.Request request
  ) {

    return ApiResponse.success(new OrderV1Dto.Create.Response(orderFacade.create(request.toCommand(userId))));
  }

  @GetMapping
  public ApiResponse<OrderV1Dto.Search.Response> search(@RequestHeader("X-USER-ID") String userId
  ) {

    return ApiResponse.success(new OrderV1Dto.Search.Response(
        List.of(new Search.Contents(1L, "주문번호1"),
            new Search.Contents(2L, "주문번호2")),
        1, 1, 1, 1
    ));
  }

  @GetMapping("/{orderId}")
  public ApiResponse<OrderV1Dto.Get.Response> get(@RequestHeader("X-USER-ID") String userId,
                                                  @PathVariable Long orderId
  ) {

    return ApiResponse.success(new OrderV1Dto.Get.Response(
        orderId, "주문번호1",
        List.of(
            new OrderItemResponse(1L, 3L, BigInteger.valueOf(10000)),
            new OrderItemResponse(2L, 2L, BigInteger.valueOf(200))
        )
    ));
  }
}
