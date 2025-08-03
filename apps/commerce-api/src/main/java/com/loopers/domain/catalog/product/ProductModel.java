package com.loopers.domain.catalog.product;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.catalog.product.embeded.ProductName;
import com.loopers.domain.catalog.product.embeded.ProductPrice;
import com.loopers.domain.catalog.product.status.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigInteger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "product")
public class ProductModel extends BaseEntity {
  private Long brandId;
  @Embedded
  private ProductName name;
  @Embedded
  private ProductPrice price;
  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToOne
  private ProductStatus status;

  public ProductModel(Long brandId, String name, BigInteger price, String description) {
    this.brandId = brandId;
    this.name = ProductName.of(name);
    this.price = ProductPrice.of(price);
    this.description = description;
  }

  public String getName() {
    return name.getName();
  }

  public BigInteger getPrice() {
    return price.getPrice();
  }
}
