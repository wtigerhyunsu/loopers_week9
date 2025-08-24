package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.payment.paymentHistory.PaymentHistoryModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment")
@Getter
public class PaymentModel extends BaseEntity {

  @Column(nullable = false, length = 25)
  private String orderNumber;


  private String transactionId;

  @Column(nullable = false, length = 25)
  private String userId;

  @Column(nullable = false)
  private BigInteger paymentAmount;

  @Column(nullable = false)
  private BigInteger orderAmount;

  //상태값
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private PaymentStatus status;

  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Builder(builderMethodName = "create")
  public PaymentModel(String orderNumber, String userId, BigInteger paymentAmount,
                      String paymentMethod,
                      String transactionId,
                      BigInteger orderAmount, String description) {
    if (orderNumber == null) {
      throw new NoSuchElementException("결제 정보에는 주문 번호가 존재해야합니다.");
    }

    if (userId == null) {
      throw new NoSuchElementException("결제 정보에는 결제가 존재해야합니다.");
    }

    this.orderNumber = orderNumber;
    this.userId = userId;
    this.paymentAmount = paymentAmount;
    this.paymentMethod = PaymentMethod.valueOf(paymentMethod);
    this.transactionId = transactionId;
    this.status = PaymentStatus.PENDING;
    this.orderAmount = orderAmount;
    this.description = description;

    if (paymentAmount == null) {
      this.paymentAmount = BigInteger.ZERO;
    }
  }

  public void changeStatus(String status) {
    this.status = PaymentStatus.valueOf(status);
  }

  public PaymentHistoryModel asHistory(String historyReason) {
    return PaymentHistoryModel.builder()
        .paymentId(getId())
        .userId(userId)
        .orderNumber(orderNumber)
        .orderAmount(orderAmount)
        .paymentAmount(paymentAmount)
        .paymentMethod(paymentMethod)
        .paymentStatus(status.name())
        .description(description)
        .reason(historyReason)
        .build();
  }

  public void failed() {
    this.status = PaymentStatus.FAILED;
  }

  public void done() {
    this.status = PaymentStatus.SUCCESS;
  }

}
