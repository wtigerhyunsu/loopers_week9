package com.loopers.application.catalog.product;

import com.loopers.domain.catalog.product.ProductProjection;
import com.loopers.domain.catalog.product.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductFacade {
  private final ProductRepository productRepository;


  /*
  latest, price_asc, likes_desc
   */

  // 목록 조회
  public ProductSearchInfo search(ProductCommand command) {
    PageRequest page = PageRequest.of(command.currentPage(), command.perSize());
    Page<ProductProjection> search = productRepository.search(command.toCriteria(), page);
    List<ProductProjection> products = search.getContent();
    return
        ProductSearchInfo.builder()
            .contents(products.stream().map(p -> ProductContents.of(
                    p.getName(), p.getBrandId(), p.getBrandName(), p.getLikedCount(), p.getPrice(), p.getCreatedAt(), p.getUpdateAt()
                ))
                .collect(Collectors.toList()))
            .build();
  }

  // 상세 조회
  public ProductGetInfo get(Long id) {
    ProductProjection productProjection = productRepository.get(id);

    return ProductGetInfo.builder()
        .productId(productProjection.getId())
        .productName(productProjection.getName())
        .brandName(productProjection.getBrandName())
        .price(productProjection.getPrice())
        .description(productProjection.getDescription())
        .likedCount(productProjection.getLikedCount())
        .createdAt(productProjection.getCreatedAt())
        .updatedAt(productProjection.getUpdateAt())
        .build();
  }

}
