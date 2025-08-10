package com.loopers.infrastructure.catalog.product.stock;

import com.loopers.domain.catalog.product.stock.StockModel;
import com.loopers.domain.catalog.product.stock.StockRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepository {
  private final StockJpaRepository stockJpaRepository;

  public StockModel get(Long productId) {
    return stockJpaRepository.findByProductIdWithLock(productId).orElseThrow(
        () -> new CoreException(ErrorType.NOT_FOUND, "상품이 존재하지 않습니다.")
    );
  }
}
