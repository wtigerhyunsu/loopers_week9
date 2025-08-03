package com.loopers.domain.catalog.brand.embeded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BrandName {
  @Column(nullable = false, unique = true, length = 50)
  private String name;

  protected BrandName() {
  }

  private BrandName(String name) {
    this.name = name;
  }

  public static BrandName of(String name) {
    return new BrandName(name);
  }

  public String getName() {
    return name;
  }

}
