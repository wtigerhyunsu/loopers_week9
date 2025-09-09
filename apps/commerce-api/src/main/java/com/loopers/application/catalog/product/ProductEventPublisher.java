package com.loopers.application.catalog.product;

public interface ProductEventPublisher {

  void send(String userId, String message);
  void fail(String userId, String failMessage);
}
