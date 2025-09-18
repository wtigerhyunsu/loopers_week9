package com.loopers.application.popularity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "popularity")
public class PopularityProperties {
  private double scalingFactor;
  private PopularityWeights weights;

  public double getScalingFactor() { return scalingFactor; }
  public void setScalingFactor(double scalingFactor) { this.scalingFactor = scalingFactor; }
  public PopularityWeights getWeights() { return weights; }
  public void setWeights(PopularityWeights weights) { this.weights = weights; }
}

