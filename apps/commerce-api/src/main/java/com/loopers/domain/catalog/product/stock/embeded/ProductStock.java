package com.loopers.domain.catalog.product.stock.embeded;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Embeddable
@Getter
@EqualsAndHashCode
public class ProductStock {
  private Long stock;

  protected ProductStock() {
  }

  private ProductStock(Long stock) {
    validate(stock);
    this.stock = stock;
  }

  public static ProductStock of(Long stock) {
    return new ProductStock(stock);
  }

  public ProductStock decrease(long stock) {
    if(stock < 0) {
      throw new CoreException(ErrorType.BAD_REQUEST, "음수로 재고 차감은 불가합니다.");
    }
    return new ProductStock(this.stock - stock);
  }

  void validate(long stock) {
    if(stock < 0) {
      throw new CoreException(ErrorType.BAD_REQUEST, "재고는 0미만일 수 없습니다.");
    }
  }

}
