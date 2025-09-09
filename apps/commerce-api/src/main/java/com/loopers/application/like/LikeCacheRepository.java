package com.loopers.application.like;

public interface LikeCacheRepository {
  void increase(Long productId);

  void decrement(Long productId);
}
