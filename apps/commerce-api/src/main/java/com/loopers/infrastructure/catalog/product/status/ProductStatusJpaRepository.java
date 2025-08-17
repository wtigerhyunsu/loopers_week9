package com.loopers.infrastructure.catalog.product.status;

import com.loopers.domain.catalog.product.status.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStatusJpaRepository extends JpaRepository<ProductStatus, Long> {
}
