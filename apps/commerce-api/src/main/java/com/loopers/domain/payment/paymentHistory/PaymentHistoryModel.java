package com.loopers.domain.payment.paymentHistory;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.payment.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigInteger;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payment_history")
@Getter
@Builder
@AllArgsConstructor
public class PaymentHistoryModel extends BaseEntity {

  @Column(nullable = false)
  private Long paymentId;

  @Column(length = 25, nullable = false)
  private String orderNumber;

  @Column(length = 20, nullable = false)
  private String userId;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String description;


  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Column(nullable = false)
  private BigInteger paymentAmount;
  @Column(nullable = false)
  private BigInteger orderAmount;

  @Column(length = 20, nullable = false)
  private String paymentStatus;

  @Column(columnDefinition = "TEXT", nullable = true)
  private String reason;
}
