package com.loopers.interfaces.api.brand;

import java.util.List;

public class BrandV1Dto {

  public record Response(
      Long brandId,
      String brandName,
      List<Product> products

  ) {

  }

  public record Product(Long productId, String productName) {}

}
