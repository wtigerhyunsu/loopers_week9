package com.loopers.application.metrics;

public interface MetricsStrategy {

  void process(String message);

  MetricsMethod method();
}
