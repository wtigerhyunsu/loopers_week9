package com.loopers.application.popularity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import com.loopers.application.popularity.PopularityCommand;

class PopularityServiceTest {

  @Test
  @DisplayName("increment like increases score by like weight")
  void increment_like() {
    RedisTemplate<String, String> redis = Mockito.mock(RedisTemplate.class);
    ZSetOperations<String, String> z = Mockito.mock(ZSetOperations.class);
    when(redis.opsForZSet()).thenReturn(z);
    PopularityProperties props = new PopularityProperties();
    PopularityService svc = new PopularityService(redis, props);

    svc.incrementLike(101L);

    verify(z).incrementScore(eq(svc.keyFor(LocalDate.now())), eq("101"), eq(1.0));
  }

  @Test
  @DisplayName("increment purchase increases by qty and amount weights")
  void increment_purchase() {
    RedisTemplate<String, String> redis = Mockito.mock(RedisTemplate.class);
    ZSetOperations<String, String> z = Mockito.mock(ZSetOperations.class);
    when(redis.opsForZSet()).thenReturn(z);
    PopularityProperties props = new PopularityProperties();
    PopularityService svc = new PopularityService(redis, props);

    svc.incrementPurchase(7L, 3, 5000);

    // qty=3, amount=5(=5000/1000), weights default 1: 3 + 5 = 8
    verify(z).incrementScore(eq(svc.keyFor(LocalDate.now())), eq("7"), eq(8.0));
  }

  @Test
  @DisplayName("topN returns entries ordered with scores")
  void topn() {
    RedisTemplate<String, String> redis = Mockito.mock(RedisTemplate.class);
    ZSetOperations<String, String> z = Mockito.mock(ZSetOperations.class);
    when(redis.opsForZSet()).thenReturn(z);
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
    PopularityService svc = new PopularityService(redis, new PopularityProperties());

    List<PopularityCommand.Entry> out = svc.topN(2, LocalDate.now());

    assertThat(out).extracting("productId").containsExactlyInAnyOrder(1L, 2L);
  }

  @Test
  @DisplayName("rankOf returns nulls when not found")
  void rank_not_found() {
    RedisTemplate<String, String> redis = Mockito.mock(RedisTemplate.class);
    ZSetOperations<String, String> z = Mockito.mock(ZSetOperations.class);
    when(redis.opsForZSet()).thenReturn(z);
    when(z.reverseRank(any(), any())).thenReturn(null);
    when(z.score(any(), any())).thenReturn(null);
    PopularityService svc = new PopularityService(redis, new PopularityProperties());

    PopularityCommand.Rank r = svc.rankOf(9L, LocalDate.now());
    assertThat(r.rank()).isNull();
    assertThat(r.score()).isNull();
  }
}
