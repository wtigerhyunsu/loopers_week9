package com.loopers.application.metrics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MetricsStrategyFactory {
  private final Map<MetricsMethod, MetricsStrategy> strategyMap = new HashMap<>();

  public MetricsStrategyFactory(List<MetricsStrategy> strategies) {
    for (MetricsStrategy strategy : strategies) {
      strategyMap.put(strategy.method(), strategy);
    }
  }

  public MetricsStrategy getStrategy(MetricsMethod method) {
    return strategyMap.get(method);
  }
}
