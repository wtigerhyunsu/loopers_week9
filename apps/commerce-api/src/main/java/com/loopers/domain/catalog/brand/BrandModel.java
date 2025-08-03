package com.loopers.domain.catalog.brand;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.catalog.brand.embeded.BrandName;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "brand")
public class BrandModel extends BaseEntity {

  private String userId;


  @Embedded
  private BrandName name;


  public BrandModel(String userId, String brandName) {
    this.userId = userId;
    this.name = BrandName.of(brandName);
  }

  public String getName() {
    return name.getName();
  }
}

