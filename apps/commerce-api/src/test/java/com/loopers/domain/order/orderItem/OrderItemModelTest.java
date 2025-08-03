package com.loopers.domain.order.orderItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("주문 아이템 생성시, ")
class OrderItemModelTest {

  @DisplayName("주문아이디는 존재하지 않는 경우, 404 Not Found를 반환합니다.")
  @Test
  void throw404NotFound_whenExitsOrderId() {
    //given
    Long orderId = null;
    Long productId = 1L;
    Long quantity = 1L;
    BigInteger price = BigInteger.valueOf(2000);

    //when
    CoreException result = assertThrows(CoreException.class, () -> OrderItemModel.builder()
        .orderId(orderId)
        .productId(productId)
        .quantity(quantity)
        .unitPrice(price)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
  }

  @DisplayName("상품아이디는 존재하지 않는 경우, 404 Not Found를 반환합니다.")
  @Test
  void throw404NotFound_whenExitsProductId() {
    //given
    Long orderId = 1L;
    Long productId = null;
    Long quantity = 1L;
    BigInteger price = BigInteger.valueOf(2000);

    //when
    CoreException result = assertThrows(CoreException.class, () -> OrderItemModel.builder()
        .orderId(orderId)
        .productId(productId)
        .quantity(quantity)
        .unitPrice(price)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
  }

  @DisplayName("수량이 존재하지 않는 경우, 400 BadRequest를 반환합니다.")
  @Test
  void throw400BadRequest_whenExitsQuantity() {
    //given
    Long orderId = 1L;
    Long productId = 1L;
    Long quantity = null;
    BigInteger price = BigInteger.valueOf(2000);

    //when
    CoreException result = assertThrows(CoreException.class, () -> OrderItemModel.builder()
        .orderId(orderId)
        .productId(productId)
        .quantity(quantity)
        .unitPrice(price)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
  }


  @DisplayName("수량이 0이하인 경우, 400 BadRequest를 반환합니다.")
  @ParameterizedTest
  @ValueSource(longs = {-1, 0})
  void throw400BadRequest_whenExitsQuantityUnderZero(long quantity) {
    //given
    Long orderId = 1L;
    Long productId = 1L;
    BigInteger price = BigInteger.valueOf(2000);

    //when
    CoreException result = assertThrows(CoreException.class, () -> OrderItemModel.builder()
        .orderId(orderId)
        .productId(productId)
        .quantity(quantity)
        .unitPrice(price)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
  }

  @DisplayName("가격이 존재하지 않는 경우, 0원을 리턴합니다.")
  @Test
  void returnZero_whenExitsPrice() {
    //given
    Long orderId = 1L;
    Long productId = 1L;
    Long quantity = 1L;

    //when
    OrderItemModel orderItem = OrderItemModel.builder()
        .orderId(orderId)
        .productId(productId)
        .quantity(quantity)
        .build();
    //then
    assertThat(orderItem.getUnitPrice()).isEqualTo(BigInteger.ZERO);
  }

  @DisplayName("모두 존재하는 경우,주문 아이템 객체를 리턴합니다.")
  @Test
  void returnOrderItem_whenAllExits() {
    //given
    Long orderId = 1L;
    Long productId = 1L;
    Long quantity = 1L;
    BigInteger price = BigInteger.valueOf(2000);

    //when
    OrderItemModel orderItem = OrderItemModel.builder()
        .orderId(orderId)
        .productId(productId)
        .quantity(quantity)
        .unitPrice(price)
        .build();

    //then
    assertAll(
        () -> assertThat(orderItem.getOrderId()).isEqualTo(orderId),
        () -> assertThat(orderItem.getProductId()).isEqualTo(productId),
        () -> assertThat(orderItem.getQuantity()).isEqualTo(quantity),
        () -> assertThat(orderItem.getUnitPrice()).isEqualTo(price)
    );

  }


}
