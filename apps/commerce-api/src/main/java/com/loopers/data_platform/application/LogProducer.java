package com.loopers.data_platform.application;

public interface LogProducer {
  void send(String userId, String message);
}
