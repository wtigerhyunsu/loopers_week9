package com.loopers.application.order;

import com.loopers.application.order.command.OrderCreateCommand;
import com.loopers.application.order.handler.OrderHistoryHandler;
import com.loopers.application.order.info.OrderCancelInfo;
import com.loopers.application.order.info.OrderCreateInfo;
import com.loopers.application.order.processor.OrderProcessor;
import com.loopers.domain.order.OrderModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacade {
  private final OrderProcessor orderProcessor;
  private final OrderHistoryHandler orderHistoryHandler;

  // 주문 생성
  @Transactional
  public OrderCreateInfo create(OrderCreateCommand command) {
    //주문서를 만들고
    OrderModel orderModel = orderProcessor.create(command);

    //히스토리 저장
    orderHistoryHandler.create(orderModel);

    //결과 리턴
    return OrderCreateInfo.from(orderModel);
  }

  //주문 취소
  @Transactional
  public OrderCancelInfo cancel(String userId, String orderNumber) {
    // 주문 취소
    OrderModel orderModel = orderProcessor.cancel(userId, orderNumber);
    // 히스토리 저장
    orderHistoryHandler.create(orderModel);
    //결과
    return OrderCancelInfo.from(orderModel);
  }

}
