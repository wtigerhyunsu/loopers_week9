package com.loopers.domain.product;

import static org.junit.Assert.assertThrows;

import com.loopers.domain.catalog.product.embeded.ProductPrice;
import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ProductModelModelTest {

  @DisplayName("상품 가격을 책정할때,")
  @Nested
  class ProductModelPriceTest {

    @DisplayName("음수라면, `IllegalArgumentException`를 반환합니다.")
    @Test
    void throwIllegalArgumentException_whenProductPriceIsUnderZero() {
      //given
      BigInteger price = BigInteger.valueOf(-1);
      // when&then
      Exception result = assertThrows(IllegalArgumentException.class, () -> ProductPrice.of(price));
    }
  }

}
