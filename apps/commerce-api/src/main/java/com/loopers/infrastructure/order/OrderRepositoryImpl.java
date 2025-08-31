package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
  private final OrderJpaRepository repository;


  @Override
  public OrderModel save(OrderModel orderModel) {
    return repository.save(orderModel);
  }

  @Override
  public OrderModel ofOrderNumber(String orderNumber) {
    return repository.findByOrderNumber(orderNumber).orElseThrow(
        () -> new CoreException(ErrorType.NOT_FOUND, "존재하지 않는 주문번호입니다.")
    );
  }
  @Override
  public OrderModel ofOrderNumber(String userId, String orderNumber) {
    return repository.findByUserIdAndOrderNumber(userId, orderNumber).orElseThrow(
        () -> new CoreException(ErrorType.CONFLICT, "해당 주문은 타인이 취소할 수 없습니다.")
    );
  }

  @Override
  public void clearDiscount(Long orderId) {
    repository.clearDiscount(orderId);
  }


}
