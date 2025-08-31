package com.loopers.infrastructure.payment;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentOrderProcessor {
  private final OrderRepository orderRepository;


  public OrderModel get(String orderNumber) {
    OrderModel orderModel = orderRepository.ofOrderNumber(orderNumber);
    orderModel.paymentCheck();
    return orderModel;
  }
}
