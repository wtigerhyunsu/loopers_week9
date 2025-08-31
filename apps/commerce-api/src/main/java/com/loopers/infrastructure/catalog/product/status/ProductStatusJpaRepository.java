package com.loopers.infrastructure.catalog.product.status;

import com.loopers.domain.catalog.product.status.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductStatusJpaRepository extends JpaRepository<ProductStatus, Long> {


  @Modifying
  @Query("""
        UPDATE ProductStatus s
           SET  s.likeCount = s.likeCount + 1
         WHERE s.productId = :productId
        """)
  void increase(Long productId);

  @Modifying
  @Query("""
        UPDATE ProductStatus s
           SET  s.likeCount = s.likeCount - 1
         WHERE s.productId = :productId
            AND s.likeCount > 0
        """)
  void decrease(Long productId);


}
