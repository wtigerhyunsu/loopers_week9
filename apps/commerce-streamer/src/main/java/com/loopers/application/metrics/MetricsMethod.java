package com.loopers.application.metrics;

public enum MetricsMethod {
  VIEWS, LIKES, SALES;

  public static MetricsMethod find(String topic) {

    if (topic.contains("VIEWS")) {
      return VIEWS;
    }
    if (topic.contains("LIKES")) {
      return LIKES;
    }
    if (topic.contains("STOCK")) {
      return SALES;
    }
    throw new IllegalArgumentException("찾으려는 타입이 존재하지 않습니다.");
  }
}
