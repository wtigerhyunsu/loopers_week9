package com.loopers.application.catalog.product;

import java.util.List;
import lombok.Builder;

public record ProductSearchInfo(
    List<ProductContents> contents,
    int page,
    int size,
    long totalElements,
    int totalPages
) {

  @Builder
  public ProductSearchInfo {
  }
}

