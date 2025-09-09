package com.loopers.domain.like;

public record LikeEvictMessage(
    Long productId,
    String key
) {
  public LikeEvictMessage(Long productId) {
    this(productId, "like:product:" + productId);
  }
}
