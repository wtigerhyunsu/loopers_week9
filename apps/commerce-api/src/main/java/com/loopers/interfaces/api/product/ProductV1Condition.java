package com.loopers.interfaces.api.product;

import com.loopers.application.catalog.product.ProductCommand;
import com.loopers.application.catalog.product.SortOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductV1Condition {
  private Long brandId;
  private String brandName;
  private String productName;
  private String sort;
  private Integer currentPage;
  private Integer perSize;

  public ProductCommand toCommand() {
    return ProductCommand.builder()
        .brandId(brandId)
        .brandName(brandName)
        .productName(productName)
        .sort(sort == null ? null : SortOption.valueOf(sort))
        .currentPage(currentPage)
        .perSize(perSize)
        .build();
  }
}
