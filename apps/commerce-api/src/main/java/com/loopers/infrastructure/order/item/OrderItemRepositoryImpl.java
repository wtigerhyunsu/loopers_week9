package com.loopers.infrastructure.order.item;

import com.loopers.domain.order.orderItem.OrderItemModel;
import com.loopers.domain.order.orderItem.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {
  private final OrderItemJpaRepository repository;


  @Override
  public void save(OrderItemModel orderItem) {
    repository.save(orderItem);
  }
}
