package com.loopers.domain.product.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.loopers.domain.catalog.product.stock.embeded.ProductStock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StockModelModelTest {

  @DisplayName("재고가 0미만이라면 `IllegalArgumentException`를 반환합니다.")
  @Test
  void throwIllegalArgumentException_whenStockIsUnderZero() {
    //given
    long stock = -1L;
    // when&then
    Exception result = assertThrows(IllegalArgumentException.class, () -> ProductStock.of(stock));
  }

  @DisplayName("재고 감소시 재고가 0미만인 경우 `IllegalArgumentException`를 반환합니다.")
  @Test
  void throw400BadRequestException_whenDecreaseStockIsUnderZero() {
    //given
    ProductStock productStock = ProductStock.of(5L);
    // when&then
    Exception result = assertThrows(IllegalArgumentException.class, () -> productStock.decrease(6L));
  }

  @DisplayName("재고 감소시 음수로 재고를 차감하는 경우에는 `IllegalArgumentException`를 반환합니다.")
  @Test
  void throw400BadRequestException_whenDecreaseNegative() {
    //given
    ProductStock productStock = ProductStock.of(5L);
    // when&then
    Exception result = assertThrows(IllegalArgumentException.class, () -> productStock.decrease(-6L));
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
