package com.loopers.domain.order.history;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 내역을 생성하는 경우,")
class OrderHistoryModelTest {

  @Test
  @DisplayName("주문 아이디가 존재하지 않는 경우, 404BadRequest를 반환합니다.")
  void throws404NotFoundException_whenNotExitsOrderId() {
    //given
    Long orderId = null;
    String orderNumber = "12345";
    String userId = "12345";
    String status = "done";
    String address = "서울시";
    String memo = "메모";
    //when
    CoreException result = assertThrows(CoreException.class, () -> OrderHistoryModel.create()
        .orderId(orderId)
        .orderNumber(orderNumber)
        .userId(userId)
        .status(status)
        .address(address)
        .memo(memo)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
  }

  @Test
  @DisplayName("주문 번호가 존재하지 않는 경우, 404BadRequest를 반환합니다.")
  void throws404NotFoundException_whenNotExitsOrderNumber() {
    //given
    Long orderId = 1L;
    String orderNumber = null;
    String userId = "12345";
    String status = "done";
    String address = "서울시";
    String memo = "메모";
    //when
    CoreException result = assertThrows(CoreException.class, () -> OrderHistoryModel.create()
        .orderId(orderId)
        .orderNumber(orderNumber)
        .userId(userId)
        .status(status)
        .address(address)
        .memo(memo)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
  }

  @Test
  @DisplayName("주문자(계정 아이디)가 존재하지 않는 경우, 404BadRequest를 반환합니다.")
  void throws404NotFoundException_whenNotExitsUserId() {
    //given
    Long orderId = 1L;
    String orderNumber = "12345";
    String userId = null;
    String status = "done";
    String address = "서울시";
    String memo = "메모";
    //when
    CoreException result = assertThrows(CoreException.class, () -> OrderHistoryModel.create()
        .orderId(orderId)
        .orderNumber(orderNumber)
        .userId(userId)
        .status(status)
        .address(address)
        .memo(memo)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
  }

  @Test
  @DisplayName("주문 상태가 존재하지 않는 경우, 404BadRequest를 반환합니다.")
  void throws404NotFoundException_whenNotExitsOrderStatus() {
    //given
    Long orderId = 1L;
    String orderNumber = "12345";
    String userId = "12345";
    String status = null;
    String address = "서울시";
    String memo = "메모";
    //when
    CoreException result = assertThrows(CoreException.class, () -> OrderHistoryModel.create()
        .orderId(orderId)
        .orderNumber(orderNumber)
        .userId(userId)
        .status(status)
        .address(address)
        .memo(memo)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
  }

  @Test
  @DisplayName("주소가 존재하지 않는 경우, 404BadRequest를 반환합니다.")
  void throws404NotFoundException_whenNotExitsAddress() {
    //given
    Long orderId = 1L;
    String orderNumber = "12345";
    String userId = "12345";
    String status = "done";
    String address = null;
    String memo = "메모";
    //when
    CoreException result = assertThrows(CoreException.class, () -> OrderHistoryModel.create()
        .orderId(orderId)
        .orderNumber(orderNumber)
        .userId(userId)
        .status(status)
        .address(address)
        .memo(memo)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
  }

  @Test
  @DisplayName("전부 존재하는 경우, 주문 내역을 리턴합니다.")
  void returnHistoryInfo_whenAllExits() {
    //given
    Long orderId = 1L;
    String orderNumber = "12345";
    String userId = "12345";
    String status = "done";
    String address = "서울시";
    String memo = "메모";
    //when
    OrderHistoryModel historyModel = OrderHistoryModel.create()
        .orderId(orderId)
        .orderNumber(orderNumber)
        .userId(userId)
        .status(status)
        .address(address)
        .memo(memo)
        .build();
    //then
    assertAll(
        () -> assertThat(historyModel.getOrderId()).isEqualTo(orderId),
        () -> assertThat(historyModel.getOrderNumber()).isEqualTo(orderNumber),
        () -> assertThat(historyModel.getStatus()).isEqualTo(status),
        () -> assertThat(historyModel.getAddress()).isEqualTo(address),
        () -> assertThat(historyModel.getMemo()).isEqualTo(memo)
    );
  }

}
