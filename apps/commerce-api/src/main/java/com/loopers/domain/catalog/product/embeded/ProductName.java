package com.loopers.domain.catalog.product.embeded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ProductName {
  @Column(nullable = false, unique = true, length = 100)
  private String name;

  public ProductName() {
  }

  public static ProductName of(String name) {
    return new ProductName(name);
  }
  private ProductName(String name) {
    this.name = name;
  }

}
