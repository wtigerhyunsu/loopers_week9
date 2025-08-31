package com.loopers.application.payment;


import com.loopers.domain.order.orderItem.OrderItemModel;
import java.math.BigInteger;
import java.util.List;

public interface PaymentPublisher {

  void publish(String orderNumber);

  void publish(Long paymentId, String result);

  void publish(String userId, BigInteger payment);


  void send(String userId, String message);
  void fail(String userId, String failMessage);

  void publish(List<OrderItemModel> orderItems);
}
