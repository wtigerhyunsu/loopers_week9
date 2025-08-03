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

  @Transactional
  public void like(String userId, Long productId) {
    ProductStatus productStatus = productRepository.has(productId).orElseThrow(
        () -> new CoreException(ErrorType.NOT_FOUND, "해당하는 상품이 존재하지 않습니다.")
    );
    Optional<LikeModel> likeModel = likeRepository.liked(userId, productId);

    // 좋아요가 되어있다면 탈출..
    if (likeModel.isPresent()) {
      return;
    }

    //좋아요
    likeRepository.like(userId, productId);
    productStatus.increase();
  }

  @Transactional
  public void unlike(String userId, Long productId) {
    ProductStatus productStatus = productRepository.has(productId).orElseThrow(
        () -> new CoreException(ErrorType.NOT_FOUND, "해당하는 상품이 존재하지 않습니다.")
    );

    Optional<LikeModel> likeModel = likeRepository.liked(userId, productId);

    // 좋아요가 해제되있다면 탈출..
    if (likeModel.isEmpty()) {
      return;
    }

    likeRepository.unlike(userId, productId);
    productStatus.decrease();
  }
}
