package com.loopers.application.popularity;

public final class PopularityCommand {
  private PopularityCommand() {}

  public record Entry(long productId, int score) {}

  public record Rank(long productId, Integer rank, Integer score) {}
}

