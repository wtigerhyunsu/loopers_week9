package com.loopers.application.catalog.product;

import java.math.BigInteger;
import java.time.ZonedDateTime;

public record ProductContents(
    String name,
    Long id,
    String brandName,
    int likeCount,
    BigInteger price
) {

  public static ProductContents of(String name, Long id,
                                   String brandName,
                                   int likeCount,
                                   BigInteger price) {
    return new ProductContents(name, id, brandName, likeCount, price);
  }
}
