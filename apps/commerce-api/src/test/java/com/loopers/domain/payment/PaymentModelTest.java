package com.loopers.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("결제 시도시, ")
class PaymentModelTest {

  @DisplayName("주문 번호가 존재하지 않는 다면, 404 NotFoundException을 발생시킨다.")
  @Test
  void throws404NotFoundException_WhenNotExitsOrderNumber() {
    //given
    String orderNumber = null;
    String userId = "userId";
    BigInteger paymentAmount = BigInteger.valueOf(2000);
    String description = "description";
    //when
    CoreException result = assertThrows(CoreException.class, () -> PaymentModel.create()
        .orderNumber(orderNumber)
        .userId(userId)
        .paymentAmount(paymentAmount)
        .description(description)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
  }

  @DisplayName("계정 아이디가 존재하지 않는 다면, 404 NotFoundException을 발생시킨다.")
  @Test
  void throws404NotFoundException_WhenNotExitsUserId() {
    //given
    String orderNumber = "12354";
    String userId = null;
    BigInteger paymentAmount = BigInteger.valueOf(2000);
    String description = "description";
    //when
    CoreException result = assertThrows(CoreException.class, () -> PaymentModel.create()
        .orderNumber(orderNumber)
        .userId(userId)
        .paymentAmount(paymentAmount)
        .description(description)
        .build());
    //then
    assertThat(result.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
  }

  @DisplayName("결제 금액이 존재하지 않는 다면,0원을 리턴한다.")
  @Test
  void returnZero_whenNotExitsPaymentAmount() {
    //given
    String orderNumber = "12354";
    String userId = "userId";
    BigInteger paymentAmount = null;
    String description = "description";
    //when
    PaymentModel paymentModel = PaymentModel.create()
        .orderNumber(orderNumber)
        .userId(userId)
        .paymentAmount(paymentAmount)
        .description(description)
        .build();
    //then
    assertThat(paymentModel.getPaymentAmount()).isEqualTo(BigInteger.ZERO);
  }

  @DisplayName("정상적으로 결제가 되어지면, 결제 정보를 리턴한다.")
  @Test
  void returnPaymentInfo_whenAllExits() {
    //given
    String orderNumber = "12354";
    String userId = "userId";
    BigInteger paymentAmount = BigInteger.valueOf(2000);
    String description = "description";
    //when
    PaymentModel paymentModel = PaymentModel.create()
        .orderNumber(orderNumber)
        .userId(userId)
        .paymentAmount(paymentAmount)
        .description(description)
        .build();
    //then
    assertAll(
        () -> assertThat(paymentModel.getOrderNumber()).isEqualTo(orderNumber),
        () -> assertThat(paymentModel.getUserId()).isEqualTo(userId),
        () -> assertThat(paymentModel.getPaymentAmount()).isEqualTo(paymentAmount),
        () -> assertThat(paymentModel.getDescription()).isEqualTo(description)
    );

  }


}
