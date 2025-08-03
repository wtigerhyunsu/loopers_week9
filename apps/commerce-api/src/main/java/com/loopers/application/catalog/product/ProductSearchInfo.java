package com.loopers.application.catalog.product;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Builder;

public record ProductSearchInfo(
    List<ProductContents> contents,
    int page,
    int size,
    int totalElements,
    int totalPages
) {

  @Builder
  public ProductSearchInfo {
  }
}

record ProductContents(
    String name,
    Long brandId,
    String brandName,
    int likeCount,
    BigInteger price,
    ZonedDateTime createdAt,
    ZonedDateTime updatedAt
) {

  public static ProductContents of(String name, Long brandId,
                                   String brandName,
                                   int likeCount,
                                   BigInteger price, ZonedDateTime createdAt,
                                   ZonedDateTime updatedAt) {
    return new ProductContents(name, brandId, brandName, likeCount, price, createdAt, updatedAt);
  }
}
