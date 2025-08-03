package com.loopers.application.catalog.brand;

import java.util.List;
import lombok.Builder;


public record BrandInfo(
    Long brandId,
    String brandName,
    List<HasProduct> products
) {
  @Builder
  public BrandInfo {
  }
}

record HasProduct(Long productId, String productName) {
  public static HasProduct of(Long productId, String productName) {
    return new HasProduct(productId, productName);
  }

}
