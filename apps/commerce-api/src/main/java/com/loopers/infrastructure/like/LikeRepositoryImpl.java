package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeModel;
import com.loopers.domain.like.LikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {
  private final LikeJpaRepository repository;

  public Optional<LikeModel> liked(String userId, Long productId) {
    return repository.findByUserIdAndProductId(userId, productId);
  }

  public void like(String userId, Long productId) {
    repository.save(LikeModel.of(userId, productId));
  }

  @Override
  public void unlike(String userId, Long productId) {
    repository.delete(userId, productId);
  }


}
