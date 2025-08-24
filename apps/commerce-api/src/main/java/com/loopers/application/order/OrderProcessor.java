package com.loopers.application.order;

import com.loopers.domain.catalog.product.ProductModel;
import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.order.orderItem.OrderItemModel;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderProcessor {
  private final ProductRepository productRepository;
  private final OrderRepository orderRepository;

  @Transactional
  public OrderModel create(OrderCreateCommand command) {
    // 계산하기 편하게 map으로 변환
    Map<Long, Long> items = command.items().stream()
        .collect(Collectors.toMap(
            OrderItemCommands::productId,
            OrderItemCommands::quantity
        ));

    // 가져와서 각각의 가격을 가져온다.
    ArrayList<Long> productIds = new ArrayList<>(items.keySet());

    // 상품 정보를 가져온다.
    List<ProductModel> products = productRepository.getIn(productIds);

    List<OrderItemModel> orderItemModels = new ArrayList<>();
    // 주문서 저장
    OrderModel orderModel = orderRepository.save(OrderModel.create()
        .userId(command.userId())
        .address(command.address())
        .memo(command.memo())
        .build());

    for (ProductModel product : products) {
      OrderItemModel orderItemModel = items.entrySet().stream().filter(entry ->
              entry.getKey().equals(product.getId()))
          .map(entry -> OrderItemModel.builder()
              .orderId(orderModel.getId())
              .productId(entry.getKey())
              .quantity(entry.getValue())
              .unitPrice(product.getPrice())
              .build()).findFirst().orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당하는 상품을 찾을 수 없습니다."));
      orderItemModels.add(orderItemModel);
    }

    orderModel.addOrderItemsAfterSave(orderItemModels);
    return orderModel;
  }

  public OrderModel cancel(String userId, String orderNumber) {
    OrderModel orderModel = orderRepository.ofOrderNumber(userId, orderNumber);
    orderModel.cancel();
    return orderModel;
  }
}
