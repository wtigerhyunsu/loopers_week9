package com.loopers.infrastructure.order.history;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.history.OrderHistoryModel;
import com.loopers.domain.order.history.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {
  private final OrderHistoryJpaRepository repository;

  @Override
  public OrderHistoryModel save(OrderModel model) {

    OrderHistoryModel historyModel = OrderHistoryModel.create()
        .orderId(model.getId())
        .orderNumber(model.getOrderNumber())
        .address(model.getAddress())
        .userId(model.getUserId())
        .status(model.getStatus().name())
        .memo(model.getMemo())
        .build();

    return repository.save(historyModel);
  }
}
