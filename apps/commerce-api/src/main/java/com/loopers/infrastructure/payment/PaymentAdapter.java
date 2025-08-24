package com.loopers.infrastructure.payment;

import com.loopers.application.payment.PaymentRequest;
import com.loopers.application.payment.PaymentResponse;
import com.loopers.domain.payment.OrderResponse;
import com.loopers.domain.payment.PaymentGateway;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentAdapter implements PaymentGateway {
  private final PaymentGatewayClient client;

  @Override
  public ApiResponse<PaymentResponse> send(String userId, PaymentRequest paymentRequest) {
    return client.send(userId, paymentRequest);
  }

  @Override
  public ApiResponse<OrderResponse> get(String userId, String orderId) {
    return client.get(userId, orderId);
  }

  @Override
  public ApiResponse<OrderResponse> get(String orderId) {
    return client.get("test", orderId);
  }
}
