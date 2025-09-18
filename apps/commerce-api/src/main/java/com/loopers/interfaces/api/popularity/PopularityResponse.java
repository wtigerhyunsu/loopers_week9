package com.loopers.interfaces.api.popularity;

import java.math.BigInteger;
import java.util.List;

public final class PopularityResponse {
  private PopularityResponse() {}

  public record TopRanking(List<RankingItem> items) {}

  public record RankingItem(long productId, String name, BigInteger price, String imageUrl, int rank) {}

  public record ProductRank(long productId, String name, BigInteger price, String imageUrl, Integer rank) {}
}
