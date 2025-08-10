package com.loopers.domain.catalog.product.embeded;

import jakarta.persistence.Embeddable;
import java.math.BigInteger;
import lombok.Getter;

@Embeddable
@Getter
public class ProductPrice {
  private BigInteger price;

  public ProductPrice() {
  }

  private ProductPrice(BigInteger price) {
    this.price = price;
  }

  public static ProductPrice of(BigInteger price) {
    validate(price);
    return new ProductPrice(price);
  }

  private static void validate(BigInteger price) {
    if (price.compareTo(BigInteger.ZERO) < 0) {
      throw new IllegalArgumentException("상품의 각겨은 음수로 책정할 수 없습니다.");
    }
  }
}
