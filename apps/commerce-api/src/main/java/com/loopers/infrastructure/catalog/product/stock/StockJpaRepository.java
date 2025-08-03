package com.loopers.infrastructure.catalog.product.stock;

import com.loopers.domain.catalog.product.stock.StockModel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends JpaRepository<StockModel, Long> {
  Optional<StockModel> findByProductId(Long productId);
}
