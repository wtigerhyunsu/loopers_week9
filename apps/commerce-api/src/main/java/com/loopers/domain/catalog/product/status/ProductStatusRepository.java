package com.loopers.domain.catalog.product.status;

public interface ProductStatusRepository {
  void increase(Long productId);

  void decrease(Long productId);
}
