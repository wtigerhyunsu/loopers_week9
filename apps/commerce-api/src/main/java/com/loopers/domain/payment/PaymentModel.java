package com.loopers.domain.payment;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

  @Column(nullable = false, length = 25)
  private String userId;

  @Column(nullable = false)
  private BigInteger paymentAmount;

  @Column(nullable = false)
  private BigInteger orderAmount;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Builder(builderMethodName = "create")
  public PaymentModel(String orderNumber, String userId, BigInteger paymentAmount,
                      BigInteger orderAmount,String description) {
    if(orderNumber == null) {
      throw new NoSuchElementException("결제 정보에는 주문 번호가 존재해야합니다.");
    }

    if(userId == null) {
      throw new NoSuchElementException("결제 정보에는 결제가 존재해야합니다.");
    }

    this.orderNumber = orderNumber;
    this.userId = userId;
    this.paymentAmount = paymentAmount;
    this.orderAmount = orderAmount;
    this.description = description;

    if(paymentAmount == null) {
      this.paymentAmount = BigInteger.ZERO;
    }
  }
}
