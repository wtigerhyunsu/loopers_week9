package com.loopers.domain.ranking;

import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.application.popularity.LikeWeight;
import com.loopers.application.popularity.PopularityProperties;
import com.loopers.application.popularity.PopularityWeights;
import com.loopers.application.popularity.PurchaseWeight;
import com.loopers.application.popularity.ViewWeight;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScoreCalculatorTest {

  @Test
  @DisplayName("daily_scaled_sum이 하루 단위로 가중치와 로그 스케일링을 적용하는지 확인")
  void daily_scaled_sum_ok() {
    PopularityProperties props = new PopularityProperties();
    props.setScalingFactor(1000.0);
    LikeWeight like = new LikeWeight(); like.setValue(1);
    PurchaseWeight purchase = new PurchaseWeight(); purchase.setQty(2); purchase.setAmountPerKrw1000(1);
    ViewWeight view = new ViewWeight(); view.setValue(1);
    PopularityWeights w = new PopularityWeights(); w.setLike(like); w.setPurchase(purchase); w.setView(view);
    props.setWeights(w);

    ScoreCalculator c = new ScoreCalculator(props);

    long[] d1 = new long[]{10, 3, 1, 5000};
    long[] d2 = new long[]{0, 0, 2, 0};

    double sum = c.dailyScaledSum(List.of(d1, d2));

    long raw1 = 1*10 + 1*3 + 2*1 + 1*(5000/1000);
    long raw2 = 1*0 + 1*0 + 2*2 + 1*(0/1000);
    double expected = Math.log10(1 + raw1) * 1000.0 + Math.log10(1 + raw2) * 1000.0;

    assertThat(sum).isEqualTo(expected);
  }
}

