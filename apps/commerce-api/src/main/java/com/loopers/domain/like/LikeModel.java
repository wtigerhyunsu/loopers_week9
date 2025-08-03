package com.loopers.domain.like;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "product_like")
public class LikeModel extends BaseEntity {

  private String userId;
  private Long productId;

  public static LikeModel of(String userId, Long productId) {
    return new LikeModel(userId, productId);
  }

  private LikeModel(String userId, Long productId) {
    if (userId == null) {
      throw new CoreException(ErrorType.BAD_REQUEST, "계정 아이디는 필수입니다.");
    }
    if (productId == null) {
      throw new CoreException(ErrorType.BAD_REQUEST, "상품 아이디는 필수입니다.");
    }

    this.userId = userId;
    this.productId = productId;
  }

}
