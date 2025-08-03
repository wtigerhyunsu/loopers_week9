package com.loopers.domain.catalog.product;

public record ProductCriteria(
    Long brandId,
    String brandName,
    String productName,
    String sort
) {
}
