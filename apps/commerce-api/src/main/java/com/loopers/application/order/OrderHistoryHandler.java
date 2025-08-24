package com.loopers.application.order;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.history.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderHistoryHandler {
  private final OrderHistoryRepository orderHistoryRepository;


  @Transactional
  public void create(OrderModel orderModel) {
    orderHistoryRepository.save(orderModel);
  }
}
