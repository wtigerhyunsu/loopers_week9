package com.loopers.application.popularity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class PopularityService {
  private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
  private final RedisTemplate<String, String> redis;
  private final PopularityProperties props;

  public PopularityService(RedisTemplate<String, String> redis, PopularityProperties props) {
    this.redis = redis;
    this.props = props;
  }

  public void incrementLike(long productId) {
    String key = keyFor(LocalDate.now());
    incr(key, productId, props.getWeights().getLike());
  }

  public void decrementLike(long productId) {
    String key = keyFor(LocalDate.now());
    incr(key, productId, -props.getWeights().getLike());
  }

  public void incrementPurchase(long productId, long quantity, long amountWon) {
    String key = keyFor(LocalDate.now());

    long qtyScore = props.getWeights()
        .getPurchase()
        .getQty() * Math.max(1, quantity);

    long scaledAmount = Math.max(0, amountWon) / 1000;

    long amountScore = props.getWeights()
        .getPurchase()
        .getAmountPerKrw1000() * scaledAmount;

    incr(key, productId, qtyScore + amountScore);
  }

  public List<PopularityCommand.Entry> topN(int limit, LocalDate date) {
    int size = Math.max(1, Math.min(limit, 100));

    String key = keyFor(date == null ? LocalDate.now() : date);

    ZSetOperations<String, String> z = redis.opsForZSet();
    var tuples = z.reverseRangeWithScores(key, 0, size - 1);

    List<PopularityCommand.Entry> out = new ArrayList<>();
    if (tuples == null || tuples.isEmpty()) {
      return out;
    }

    Double max = z.reverseRangeWithScores(key, 0, 0)
        .stream()
        .findFirst()
        .map(ZSetOperations.TypedTuple::getScore)
        .orElse(0.0);

    double denom = max != null && max > 0 ? max : 1.0;

    for (ZSetOperations.TypedTuple<String> t : tuples) {
      if (t.getValue() == null || t.getScore() == null) {
        continue;
      }

      int normalized = normalizeToInt(t.getScore(), denom);
      out.add(new PopularityCommand.Entry(Long.parseLong(t.getValue()), normalized));
    }

    return out;
  }

  public PopularityCommand.Rank rankOf(long productId, LocalDate date) {
    String key = keyFor(date == null ? LocalDate.now() : date);

    ZSetOperations<String, String> z = redis.opsForZSet();

    Long rank = z.reverseRank(key, Long.toString(productId));
    Double score = z.score(key, Long.toString(productId));
    if (rank == null || score == null) {
      return new PopularityCommand.Rank(productId, null, null);
    }

    Double max = z.reverseRangeWithScores(key, 0, 0)
        .stream()
        .findFirst()
        .map(ZSetOperations.TypedTuple::getScore)
        .orElse(0.0);

    double denom = max != null && max > 0 ? max : 1.0;

    int normalized = normalizeToInt(score, denom);

    return new PopularityCommand.Rank(productId, rank.intValue() + 1, normalized);
  }

  private void incr(String key, long productId, long score) {
    ZSetOperations<String, String> z = redis.opsForZSet();

    z.incrementScore(key, Long.toString(productId), score);

    redis.expire(key, java.time.Duration.ofHours(props.getKeyTtlHours()));
  }

  public String keyFor(LocalDate date) {
    Objects.requireNonNull(date);
    return "pop:product:" + DATE.format(date);
  }

  private int normalizeToInt(double score, double denom) {
    double v = Math.max(0d, score / denom);

    int scaled = (int) Math.round(v * 10_000d);

    if (scaled < 0) return 0;

    if (scaled > 10_000) return 10_000;

    return scaled;
  }

}
