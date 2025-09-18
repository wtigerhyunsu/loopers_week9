package com.loopers.domain.ranking;

import com.loopers.application.popularity.PopularityProperties;
import java.util.Collection;
import org.springframework.stereotype.Component;

@Component
public class ScoreCalculator {
  private final PopularityProperties props;

  public ScoreCalculator(PopularityProperties props) {
    this.props = props;
  }

  public double dailyScaledSum(Collection<long[]> dailyValues) {
    double sum = 0.0;
    for (long[] v : dailyValues) {
      long views = v[0];
      long likes = v[1];
      long sales = v[2];
      long amount = v[3];
      long raw = props.getWeights().getView().getValue() * views
          + props.getWeights().getLike().getValue() * likes
          + props.getWeights().getPurchase().getQty() * sales
          + props.getWeights().getPurchase().getAmountPerKrw1000() * Math.max(0, amount) / 1000;
      double scaled = Math.log10(1 + Math.max(0, raw)) * props.getScalingFactor();
      sum += scaled;
    }
    return sum;
  }
}
