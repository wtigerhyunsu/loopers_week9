package com.loopers.application.catalog.product;

import com.loopers.domain.catalog.product.ProductCriteria;
import lombok.Builder;

@Builder
public record ProductCommand(
    Long brandId,
    String brandName,
    String productName,
    SortOption sort,
    Integer currentPage,
    Integer perSize
) {

  public ProductCommand {
    if(sort == null) {
      sort = SortOption.LATEST;
    }
    // 기본 페이지수는 0부터
    if(currentPage == null) {
      currentPage = 0;
    }

    // 기본 페이지 내용 갯수는 10개
    if(perSize == null) {
      perSize = 10;
    }
  }

  public ProductCriteria toCriteria() {
    return new ProductCriteria(
        brandId,
        brandName,
        productName,
        sort.name());
  }

}

