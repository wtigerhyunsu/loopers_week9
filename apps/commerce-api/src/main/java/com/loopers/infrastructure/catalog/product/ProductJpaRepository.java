package com.loopers.infrastructure.catalog.product;

import com.loopers.domain.catalog.product.ProductModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductModel, Long> {
  List<ProductModel> findByBrandId(Long brandId);
}
