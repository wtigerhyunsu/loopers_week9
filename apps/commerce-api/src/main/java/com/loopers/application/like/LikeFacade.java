package com.loopers.application.like;

import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.domain.catalog.product.status.ProductStatus;
import com.loopers.domain.like.LikeModel;
import com.loopers.domain.like.LikeRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeFacade {
  private final ProductRepository productRepository;
  private final LikeRepository likeRepository;
  private final LikeEventPublisher publisher;

  @Transactional
  public void like(String userId, Long productId) {
    try {
      ProductStatus productStatus = productRepository.hasWithLock(productId).orElseThrow(
          () -> new CoreException(ErrorType.NOT_FOUND, "해당하는 상품이 존재하지 않습니다.")
      );
      Optional<LikeModel> likeModel = likeRepository.liked(userId, productId);

      // 좋아요가 되어있다면 탈출..
      if (likeModel.isPresent()) {
        return;
      }

      //좋아요
      likeRepository.like(userId, productId);

      // 좋아요 증가
      publisher.increase(userId, productId);
      publisher.send(userId, "INCREASE", userId + "가 productId : " + productId + "에 좋아요를 눌렀습니다.");
    } catch (Exception e) {
      publisher.fail(userId, "INCREASE", e.getMessage());
    }

  }

  @Transactional
  public void unlike(String userId, Long productId) {
    try {
      ProductStatus productStatus = productRepository.hasWithLock(productId).orElseThrow(
          () -> new CoreException(ErrorType.NOT_FOUND, "해당하는 상품이 존재하지 않습니다.")
      );

      Optional<LikeModel> likeModel = likeRepository.liked(userId, productId);

      // 좋아요가 해제되있다면 탈출..
      if (likeModel.isEmpty()) {
        return;
      }

      likeRepository.unlike(userId, productId);

      // 좋아요 감소
      publisher.decrease(userId, productId);
      publisher.send(userId, "DECREASE", userId + "가 productId : " + productId + "에 좋아요를 해제했습니다.");
    } catch (Exception e) {
      publisher.fail(userId, "DECREASE", e.getMessage());
    }

  }
}
