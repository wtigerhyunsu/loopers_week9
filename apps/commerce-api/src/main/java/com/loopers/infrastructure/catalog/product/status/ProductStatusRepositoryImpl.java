package com.loopers.infrastructure.catalog.product.status;

import com.loopers.domain.catalog.product.status.ProductStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductStatusRepositoryImpl implements ProductStatusRepository {
  private final ProductStatusJpaRepository  productStatusJpaRepository;

}
