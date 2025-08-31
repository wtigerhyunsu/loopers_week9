package com.loopers.application.catalog.product;

import com.loopers.domain.catalog.product.ProductProjection;
import com.loopers.domain.catalog.product.ProductRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductFacade {
  private final ProductRepository productRepository;
  private final ProductWarmupProcessor warmupProcessor;
  private final ProductPublisher publisher;

  /*
  latest, price_asc, likes_desc
   */

  // 목록 조회
  public ProductSearchInfo search(ProductCommand command) {
    Page<ProductProjection> searchData = warmupProcessor.searchData(command);
    List<ProductProjection> products = searchData.getContent();
    return
        ProductSearchInfo.builder()
            .contents(products.stream().map(p -> ProductContents.of(
                    p.getName(), p.getId(), p.getBrandName(), p.getLikedCount(), p.getPrice()
                ))
                .collect(Collectors.toList()))
            .page(searchData.getNumber())
            .size(searchData.getSize())
            .totalPages(searchData.getTotalPages())
            .totalElements(searchData.getTotalElements())
            .build();
  }


  // 상세 조회
  public ProductGetInfo get(String userId, Long id) {
    try {
      ProductProjection productProjection = productRepository.get(id);
      publisher.send(userId, userId + "가 productId : " + id + "를 조회 했습니다.");
      return ProductGetInfo.builder()
          .productId(productProjection.getId())
          .productName(productProjection.getName())
          .brandName(productProjection.getBrandName())
          .price(productProjection.getPrice())
          .description(productProjection.getDescription())
          .likedCount(productProjection.getLikedCount())
          .build();
    }catch (CoreException e) {
      throw e;
    } catch (Exception e) {
      publisher.fail(userId, e.getMessage());
      throw new CoreException(ErrorType.INTERNAL_ERROR, e.getMessage());
    }

  }

  public void rank() {
    warmupProcessor.warmup();
  }
}
