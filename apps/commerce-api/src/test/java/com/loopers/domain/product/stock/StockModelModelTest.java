package com.loopers.domain.product.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.loopers.domain.catalog.product.stock.embeded.ProductStock;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StockModelModelTest {

  @DisplayName("재고가 0미만이라면 `400 Bad Request`를 반환합니다.")
  @Test
  void throw400BadRequestException_whenStockIsUnderZero() {
    //given
    long stock = -1L;
    // when&then
    CoreException result = assertThrows(CoreException.class, () -> ProductStock.of(stock));
    assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
  }

  @DisplayName("재고 감소시 재고가 0미만인 경우 `400 Bad Request`를 반환합니다.")
  @Test
  void throw400BadRequestException_whenDecreaseStockIsUnderZero() {
    //given
    ProductStock productStock = ProductStock.of(5L);
    // when&then
    CoreException result = assertThrows(CoreException.class, () -> productStock.decrease(6L));
    assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
  }

  @DisplayName("재고 감소시 음수로 재고를 차감하는 경우에는 `400 Bad Request`를 반환합니다.")
  @Test
  void throw400BadRequestException_whenDecreaseNegative() {
    //given
    ProductStock productStock = ProductStock.of(5L);
    // when&then
    CoreException result = assertThrows(CoreException.class, () -> productStock.decrease(-6L));
    assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
  }

  @DisplayName("재고 정상적으로 감소시킨 경우 재고 객체가 리턴이 되어집니다.")
  @Test
  void returnProductStock_whenDecreasePositive() {
    //given
    ProductStock productStock = ProductStock.of(10L);
    ProductStock decreaseStock = ProductStock.of(5L);
    // when
    ProductStock resultStock = productStock.decrease(5L);
    //then
    assertThat(resultStock).isEqualTo(decreaseStock);
  }


}
