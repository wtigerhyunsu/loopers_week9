package com.loopers.application.popularity;

import com.loopers.domain.catalog.product.ProductModel;
import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.interfaces.api.popularity.PopularityResponse;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class PopularityService {
  private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
  private final RedisTemplate<String, String> redis;
  private final PopularityProperties props;
  private final ProductRepository productRepository;

  public PopularityService(RedisTemplate<String, String> redis, PopularityProperties props,
                          ProductRepository productRepository) {
    this.redis = redis;
    this.props = props;
    this.productRepository = productRepository;
  }

  public void incrementLike(long productId) {
    String key = keyFor(LocalDate.now());
    incr(key, productId, props.getWeights().getLike().getValue());
  }

  public void decrementLike(long productId) {
    String key = keyFor(LocalDate.now());
    incr(key, productId, -props.getWeights().getLike().getValue());
  }

  public void incrementPurchase(long productId, long quantity, long amountWon) {
    String key = keyFor(LocalDate.now());

    long qtyScore = props.getWeights().getPurchase().getQty() * Math.max(1, quantity);
    long scaledAmount = Math.max(0, amountWon) / 1000;
    long amountScore = props.getWeights().getPurchase().getAmountPerKrw1000() * scaledAmount;

    incr(key, productId, qtyScore + amountScore);
  }

  public PopularityResponse.TopRanking topN(int limit, LocalDate date) {
    int size = Math.max(1, Math.min(limit, 100));

    String key = keyFor(date == null ? LocalDate.now() : date);

    ZSetOperations<String, String> z = redis.opsForZSet();
    var tuples = z.reverseRangeWithScores(key, 0, size - 1);

    List<PopularityResponse.RankingItem> items = new ArrayList<>();
    if (tuples == null || tuples.isEmpty()) {
      return new PopularityResponse.TopRanking(items);
    }

    List<Long> productIds = tuples.stream()
        .filter(t -> t.getValue() != null)
        .map(t -> Long.parseLong(t.getValue()))
        .toList();

    Map<Long, ProductModel> productMap = productRepository.getIn(productIds)
        .stream()
        .collect(Collectors.toMap(ProductModel::getId, Function.identity()));

    List<ZSetOperations.TypedTuple<String>> ordered = new ArrayList<>(tuples);
    Double prevScore = null;
    int currentRank = 0;
    for (ZSetOperations.TypedTuple<String> t : ordered) {
      if (t.getValue() == null || t.getScore() == null) {
        continue;
      }

      long productId = Long.parseLong(t.getValue());
      ProductModel product = productMap.get(productId);
      if (product == null) {
        continue;
      }

      if (prevScore == null || Double.compare(t.getScore(), prevScore) != 0) {
        currentRank += 1;
        prevScore = t.getScore();
      }

      items.add(new PopularityResponse.RankingItem(
          productId,
          product.getName(),
          product.getPrice(),
          null, // imageUrl not available in ProductModel
          currentRank
      ));
    }

    return new PopularityResponse.TopRanking(items);
  }

  public PopularityResponse.ProductRank rankOf(long productId, LocalDate date) {
    String key = keyFor(date == null ? LocalDate.now() : date);

    ZSetOperations<String, String> z = redis.opsForZSet();

    Long rank = z.reverseRank(key, Long.toString(productId));

    List<ProductModel> products = productRepository.getIn(List.of(productId));
    ProductModel product = products.isEmpty() ? null : products.get(0);

    if (rank == null || product == null) {
      String productName = product != null ? product.getName() : "Unknown Product";
      BigInteger productPrice = product != null ? product.getPrice() : BigInteger.ZERO;
      return new PopularityResponse.ProductRank(productId, productName, productPrice, null, null);
    }

    // compute dense rank: count distinct score groups up to this index
    var upTo = z.reverseRangeWithScores(key, 0, rank);
    int denseRank = 0;
    if (upTo != null && !upTo.isEmpty()) {
      Double prev = null;
      for (ZSetOperations.TypedTuple<String> t : new ArrayList<>(upTo)) {
        if (t.getScore() == null) continue;
        if (prev == null || Double.compare(t.getScore(), prev) != 0) {
          denseRank += 1;
          prev = t.getScore();
        }
      }
    }

    return new PopularityResponse.ProductRank(
        productId,
        product.getName(),
        product.getPrice(),
        null,
        denseRank
    );
  }

  private void incr(String key, long productId, long rawScore) {
    ZSetOperations<String, String> z = redis.opsForZSet();

    // apply logarithmic scaling at storage time
    double scaledScore = Math.log10(1 + Math.max(0, rawScore)) * props.getScalingFactor();

    z.incrementScore(key, Long.toString(productId), scaledScore);
    redis.expire(key, java.time.Duration.ofHours(props.getKeyTtlHours()));
  }

  public String keyFor(LocalDate date) {
    Objects.requireNonNull(date);
    return "pop:product:" + DATE.format(date);
  }


}
