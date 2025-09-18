package com.loopers.application.popularity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.interfaces.api.popularity.PopularityResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

class PopularityServiceTest {

  @Test
  @DisplayName("increment like increases score by like weight")
  void increment_like() {
    RedisTemplate<String, String> redis = Mockito.mock(RedisTemplate.class);
    ZSetOperations<String, String> z = Mockito.mock(ZSetOperations.class);
    when(redis.opsForZSet()).thenReturn(z);
    PopularityProperties props = new PopularityProperties();
    props.setKeyTtlHours(24);
    props.setScalingFactor(1000.0);
    LikeWeight likeWeight = new LikeWeight();
    likeWeight.setValue(1);
    PurchaseWeight purchaseWeight = new PurchaseWeight();
    purchaseWeight.setQty(1);
    purchaseWeight.setAmountPerKrw1000(1);
    ViewWeight viewWeight = new ViewWeight();
    viewWeight.setValue(1);
    PopularityWeights weights = new PopularityWeights();
    weights.setLike(likeWeight);
    weights.setPurchase(purchaseWeight);
    weights.setView(viewWeight);
    props.setWeights(weights);
    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    PopularityService svc = new PopularityService(redis, props, productRepository);

    svc.incrementLike(101L);

    // verify logarithmic scaling is applied: log10(1 + 1) * 1000.0 = log10(2) * 1000 ≈ 301.03
    verify(z).incrementScore(eq(svc.keyFor(LocalDate.now())), eq("101"), eq(Math.log10(2) * 1000.0));
  }

  @Test
  @DisplayName("increment purchase increases by qty and amount weights")
  void increment_purchase() {
    RedisTemplate<String, String> redis = Mockito.mock(RedisTemplate.class);
    ZSetOperations<String, String> z = Mockito.mock(ZSetOperations.class);
    when(redis.opsForZSet()).thenReturn(z);
    PopularityProperties props = new PopularityProperties();
    props.setKeyTtlHours(24);
    props.setScalingFactor(1000.0);
    LikeWeight likeWeight = new LikeWeight();
    likeWeight.setValue(1);
    PurchaseWeight purchaseWeight = new PurchaseWeight();
    purchaseWeight.setQty(1);
    purchaseWeight.setAmountPerKrw1000(1);
    ViewWeight viewWeight = new ViewWeight();
    viewWeight.setValue(1);
    PopularityWeights weights = new PopularityWeights();
    weights.setLike(likeWeight);
    weights.setPurchase(purchaseWeight);
    weights.setView(viewWeight);
    props.setWeights(weights);
    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    PopularityService svc = new PopularityService(redis, props, productRepository);

    svc.incrementPurchase(7L, 3, 5000);

    // qty=3, amount=5(=5000/1000), weights default 1: 3 + 5 = 8
    // logarithmic scaling: log10(1 + 8) * 1000.0 = log10(9) * 1000 ≈ 954.24
    verify(z).incrementScore(eq(svc.keyFor(LocalDate.now())), eq("7"), eq(Math.log10(9) * 1000.0));
  }

  @Test
  @DisplayName("topN returns entries ordered with scores")
  void topn() {
    RedisTemplate<String, String> redis = Mockito.mock(RedisTemplate.class);
    ZSetOperations<String, String> z = Mockito.mock(ZSetOperations.class);
    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    when(redis.opsForZSet()).thenReturn(z);

    // mock product data
    when(productRepository.getIn(any())).thenReturn(List.of());

    ZSetOperations.TypedTuple<String> t1 = new ZSetOperations.TypedTuple<>() {
      public String getValue() { return "1"; }
      public Double getScore() { return 10.0; }
      public int compareTo(ZSetOperations.TypedTuple<String> o) { return 0; }
    };
    ZSetOperations.TypedTuple<String> t2 = new ZSetOperations.TypedTuple<>() {
      public String getValue() { return "2"; }
      public Double getScore() { return 5.0; }
      public int compareTo(ZSetOperations.TypedTuple<String> o) { return 0; }
    };
    when(z.reverseRangeWithScores(any(), eq(0L), eq(1L))).thenReturn(Set.of(t1, t2));

    PopularityProperties props = new PopularityProperties();
    props.setKeyTtlHours(24);
    props.setScalingFactor(1000.0);
    LikeWeight likeWeight = new LikeWeight();
    likeWeight.setValue(1);
    PurchaseWeight purchaseWeight = new PurchaseWeight();
    purchaseWeight.setQty(1);
    purchaseWeight.setAmountPerKrw1000(1);
    ViewWeight viewWeight = new ViewWeight();
    viewWeight.setValue(1);
    PopularityWeights weights = new PopularityWeights();
    weights.setLike(likeWeight);
    weights.setPurchase(purchaseWeight);
    weights.setView(viewWeight);
    props.setWeights(weights);
    PopularityService svc = new PopularityService(redis, props, productRepository);

    PopularityResponse.TopRanking result = svc.topN(2, LocalDate.now());

    // since no products are returned from repository, result should be empty
    assertThat(result.items()).isEmpty();
  }

  @Test
  @DisplayName("rankOf returns nulls when not found")
  void rank_not_found() {
    RedisTemplate<String, String> redis = Mockito.mock(RedisTemplate.class);
    ZSetOperations<String, String> z = Mockito.mock(ZSetOperations.class);
    ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    when(redis.opsForZSet()).thenReturn(z);
    when(z.reverseRank(any(), any())).thenReturn(null);
    // score is no longer queried for API response
    when(productRepository.getIn(any())).thenReturn(List.of());

    PopularityProperties props = new PopularityProperties();
    props.setKeyTtlHours(24);
    props.setScalingFactor(1000.0);
    LikeWeight likeWeight = new LikeWeight();
    likeWeight.setValue(1);
    PurchaseWeight purchaseWeight = new PurchaseWeight();
    purchaseWeight.setQty(1);
    purchaseWeight.setAmountPerKrw1000(1);
    ViewWeight viewWeight = new ViewWeight();
    viewWeight.setValue(1);
    PopularityWeights weights = new PopularityWeights();
    weights.setLike(likeWeight);
    weights.setPurchase(purchaseWeight);
    weights.setView(viewWeight);
    props.setWeights(weights);
    PopularityService svc = new PopularityService(redis, props, productRepository);

    PopularityResponse.ProductRank r = svc.rankOf(9L, LocalDate.now());
    assertThat(r.rank()).isNull();
  }
}
