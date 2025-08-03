package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.loopers.domain.catalog.product.embeded.ProductPrice;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ProductModelModelTest {

  @DisplayName("상품 가격을 책정할때,")
  @Nested
  class ProductModelPriceTest {

    @DisplayName("음수라면, `400 Bad Request`를 반환합니다.")
    @Test
    void throw400BadRequestException_whenProductPriceIsUnderZero() {
      //given
      BigInteger price = BigInteger.valueOf(-1);
      // when&then
      CoreException result = assertThrows(CoreException.class, () -> ProductPrice.of(price));
      assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
    }
  }

}
