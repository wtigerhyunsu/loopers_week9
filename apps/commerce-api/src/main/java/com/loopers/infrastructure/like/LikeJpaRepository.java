package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeJpaRepository extends JpaRepository<LikeModel, Long> {

  Optional<LikeModel> findByUserIdAndProductId(String UserId, Long ProductId);

  @Modifying
  @Query("delete from LikeModel l where l.userId=:userId and l.productId =:productId")
  void delete(String userId, Long productId);
}
