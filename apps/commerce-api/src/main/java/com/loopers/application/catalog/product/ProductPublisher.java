package com.loopers.application.catalog.product;

public interface ProductPublisher {

  void send(String userId, String message);
  void fail(String userId, String failMessage);
}
