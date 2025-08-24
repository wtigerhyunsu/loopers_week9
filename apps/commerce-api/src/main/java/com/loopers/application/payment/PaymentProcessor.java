package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.PaymentRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProcessor {
  private final PaymentRepository paymentRepository;

  public PaymentModel create(PaymentProcessorVo vo) {
    return paymentRepository.save(PaymentModel.create()
        .userId(vo.userId())
        .orderNumber(vo.orderNumber())
        .transactionId(vo.transactionKey())
        .description(vo.description())
        .paymentMethod(vo.paymentMethod())
        .orderAmount(vo.payment())
        .paymentAmount(vo.totalPrice())
        .build());

  }

  public PaymentModel get(String transactionId) {
    return paymentRepository.get(transactionId).orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당하는 결제는 존재하지 않습니다."));
  }
}
