package com.loopers.infrastructure.catalog.product.status;

import com.loopers.domain.catalog.product.status.ProductStatusRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductStatusRepositoryImpl implements ProductStatusRepository {
  private final ProductStatusJpaRepository productStatusJpaRepository;


  @Transactional
  public void increase(Long productId) {
    productStatusJpaRepository.increase(productId);
  }

  @Transactional
  public void decrease(Long productId) {
    productStatusJpaRepository.decrease(productId);
  }

}
