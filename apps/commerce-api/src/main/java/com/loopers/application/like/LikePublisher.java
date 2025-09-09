package com.loopers.application.like;

public interface LikePublisher {
  void aggregate(Long productId, int data);
  void evict(Long productId);
}
