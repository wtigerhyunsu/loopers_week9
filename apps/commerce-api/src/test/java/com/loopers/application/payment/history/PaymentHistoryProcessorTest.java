package com.loopers.application.payment.history;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.loopers.application.payment.PaymentHistoryProcessor;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.paymentHistory.PaymentHistoryModel;
import com.loopers.infrastructure.payment.PaymentJpaRepository;
import com.loopers.infrastructure.payment.paymentHistory.PaymentHistoryJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import java.math.BigInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentHistoryProcessorTest {

  @Autowired
  private PaymentHistoryJpaRepository repository;

  @Autowired
  private PaymentHistoryProcessor processor;

  @Autowired
  private PaymentJpaRepository paymentJpaRepository;

  @Autowired
  private DatabaseCleanUp databaseCleanUp;

  @AfterEach
  void tearDown() {
    databaseCleanUp.truncateAllTables();
  }

  @DisplayName("결제 내역을 저장합니다.")
  @Test
  void savePaymentHistory_whenSavePaymentHistory() {
    //given
    PaymentModel paymentModel = paymentJpaRepository.save(PaymentModel.create()
        .orderNumber("주문번호")
        .userId("userId")
        .paymentMethod(PaymentMethod.POINT.name())
        .paymentAmount(BigInteger.TEN)
        .orderAmount(BigInteger.TEN)
        .description("이래서 결제함")
        .build());
    //when
    processor.add(paymentModel, "reason");
    //then
    PaymentHistoryModel dbModel = repository.findByPaymentId((paymentModel.getId())).get();
    assertAll(
        () -> assertEquals(paymentModel.getId(), dbModel.getPaymentId()),
        () -> assertEquals(paymentModel.getOrderNumber(), dbModel.getOrderNumber()),
        () -> assertEquals(paymentModel.getUserId(), dbModel.getUserId()),
        () -> assertEquals(paymentModel.getPaymentAmount(), dbModel.getPaymentAmount()),
        () -> assertEquals(paymentModel.getOrderAmount(), dbModel.getOrderAmount()),
        () -> assertEquals(paymentModel.getDescription(), dbModel.getDescription()),
        () -> assertEquals("reason", dbModel.getReason())
    );

  }
}
