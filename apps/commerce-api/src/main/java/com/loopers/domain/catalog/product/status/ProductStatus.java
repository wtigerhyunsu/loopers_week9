package com.loopers.domain.catalog.product.status;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.NoSuchElementException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_status")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductStatus extends BaseEntity {
  private Long productId;
  private int likeCount;


  private ProductStatus(Long productId) {
    if (productId == null) {
      throw new NoSuchElementException("상품ID가 존재하지 않는다면 좋아요를 증감할 수 없습니다");
    }
    this.productId = productId;
  }

  private ProductStatus(Long productId, int likeCount) {
    this(productId);
    this.likeCount = likeCount;
  }

  public static ProductStatus register(Long productId) {
    return new ProductStatus(productId, 0);
  }

  public static ProductStatus of(Long productId, int count) {
    return new ProductStatus(productId, count);
  }

  public void increase() {
    this.likeCount += 1;
  }

  public void decrease() {
    int likeCurrentCount = this.likeCount - 1;

    if (likeCurrentCount < 0) {
      throw new IllegalArgumentException("좋아요 count는 0미만이 될 수 없습니다.");
    }

    this.likeCount = likeCurrentCount;
  }
}
