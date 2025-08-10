package com.loopers.domain.catalog.product;

import com.loopers.domain.catalog.product.status.ProductStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
  List<ProductModel> listOf(Long brandId);
  Page<ProductProjection> search(ProductCriteria criteria, Pageable pageable);
  ProductProjection get(Long productId);

  List<ProductModel> getIn(List<Long> productIds);

  Optional<ProductStatus> has(Long productId);

  Optional<ProductStatus> hasWithLock(Long productId);

}
