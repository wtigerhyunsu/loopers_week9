package com.loopers.infrastructure.payment;

import com.loopers.application.payment.PaymentGatewayCommand;
import com.loopers.application.payment.PaymentGatewayPort;
import com.loopers.application.payment.PaymentRequest;
import com.loopers.application.payment.PaymentResponse;
import com.loopers.domain.payment.OrderResponse;
import com.loopers.domain.payment.PaymentModel;
import com.loopers.domain.payment.PaymentRepository;
import com.loopers.domain.payment.TransactionStatusResponse;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentGatewayPortImpl implements PaymentGatewayPort {
  private final PaymentAdapter paymentAdapter;
  private final PaymentRepository paymentRepository;

  @Retry(name = "pg-payment", fallbackMethod = "handlePaymentFailure")
  @CircuitBreaker(name = "pg-payment", fallbackMethod = "handlePaymentFailure")
  public PaymentResponse send(PaymentGatewayCommand command) {
    // Resilience4j가 예외를 감지하여 fallbackMethod를 자동으로 호출하므로 try-catch 블록을 제거합니다.
    return paymentAdapter.send(
        command.userId(),
        new PaymentRequest(command, "http://localhost:8080/api/v1/payment/callback")
    ).data();
  }

  @Retry(name = "get-payment", fallbackMethod = "handlePaymentGetFallback")
  @CircuitBreaker(name = "get-payment", fallbackMethod = "handlePaymentGetFallback")
  public OrderResponse get(String orderId) {
    return paymentAdapter.get(orderId).data();
  }

  private PaymentResponse handlePaymentFailure(PaymentGatewayCommand command, Throwable ex) {
    log.error("PG 요청 실패: orderId={}, userId={}", command.orderId(), command.userId(), ex);
    throw new CoreException(ErrorType.INTERNAL_ERROR, "결제 요청 실패:" + ex.getMessage());
  }


  private OrderResponse handlePaymentGetFallback(
      String orderId, Throwable ex) {
    log.warn("PG 조회 실패에 대한 Fallback 실행: orderId={}, 원인: {}", orderId, ex.getMessage());
    List<PaymentModel> payments = paymentRepository.getAll(orderId);

    if (payments.isEmpty()) {
      // 외부 시스템도 안되고, 내부 DB에도 정보가 없으면 그 때 예외를 던집니다.
      throw new CoreException(ErrorType.INTERNAL_ERROR, "결제 정보 조회에 실패했고, 내부 DB에도 정보가 없습니다." + ex.getMessage());
    }

    // 외부 시스템 조회는 실패했지만, 내부 DB 데이터로 응답을 구성하여 반환합니다.
    log.info("PG 조회는 실패했으나, 내부 DB 정보로 응답을 대체합니다. orderId={}", orderId);
    List<OrderResponse.TransactionResponse> transactions = payments.stream()
        .map(p -> new OrderResponse.TransactionResponse(
            p.getTransactionId(),
            TransactionStatusResponse.valueOf(p.getStatus().name()),
            "FALLBACK_FROM_DB" // 상태를 명확히 알려줍니다.
        ))
        .toList();
    return new OrderResponse(orderId, transactions);
  }

}
