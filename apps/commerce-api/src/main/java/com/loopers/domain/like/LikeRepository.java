package com.loopers.domain.like;

import java.util.Optional;

public interface LikeRepository {

  Optional<LikeModel> liked(String userId, Long productId);
  void like(String userId, Long productId);

  void unlike(String userId, Long productId);
}
