package com.loopers.domain.order;

public interface OrderPublisher {
  void publish(String userId, Long orderId, Long couponId);

  void publish(Long orderId, String payload);

  void send(String userId, String message);
  void fail(String userId, String failMessage);
}
