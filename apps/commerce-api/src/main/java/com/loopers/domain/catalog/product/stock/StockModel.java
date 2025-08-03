package com.loopers.domain.catalog.product.stock;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.catalog.product.stock.embeded.ProductStock;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "stock")
public class StockModel extends BaseEntity {
  private Long productId;
  @Embedded
  private ProductStock stock;

  public StockModel(Long productId, Long stock) {
    this.productId = productId;
    this.stock = ProductStock.of(stock);
  }

  public void decrease(Long value) {
    this.stock = stock.decrease(value);
  }

  public Long stock() {
    return stock.getStock();
  }
}
