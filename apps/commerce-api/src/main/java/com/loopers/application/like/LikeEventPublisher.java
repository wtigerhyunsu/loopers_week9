package com.loopers.application.like;

public interface LikeEventPublisher {
  void increase(String userId, Long productId);

  void decrease(String userId, Long productId);

  void send(String userId, String type, String message);
  void fail(String userId, String type, String failReason);
}
