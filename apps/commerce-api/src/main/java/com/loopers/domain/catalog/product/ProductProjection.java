package com.loopers.domain.catalog.product;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ProductProjection {
  private Long id;
  private Long brandId;
  private String brandName;
  private String name;
  private BigInteger price;
  private String description;
  private int likedCount;


  @QueryProjection
  public ProductProjection(Long id, Long brandId, String brandName, String name, BigInteger price, String description,
                           int likedCount) {
    this.id = id;
    this.brandId = brandId;
    this.brandName = brandName;
    this.name = name;
    this.price = price;
    this.description = description;
    this.likedCount = likedCount;
  }
}
